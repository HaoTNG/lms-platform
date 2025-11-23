package com.example.lms.service.imple;

import com.example.lms.dto.Pagination;
import com.example.lms.enums.CourseStatus;
import com.example.lms.enums.RecipientType;
import com.example.lms.enums.ReportTicketStatus;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import com.example.lms.dto.Response;
import com.example.lms.security.JwtUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.lms.dto.UserDTO;
import com.example.lms.mapper.AnnouncementMapper;
import com.example.lms.mapper.UserMapper;
import com.example.lms.dto.AnnouncementDTO;
import com.example.lms.model.Course;
import com.example.lms.model.Announcement;
import com.example.lms.model.AnnouncementUser;
import com.example.lms.model.ReportTicket;
import com.example.lms.model.User;
import com.example.lms.model.Admin;
import com.example.lms.repository.*;
import com.example.lms.service.interf.AdminService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class AdminServiceImple implements AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CourseRepository courseRepository;
    private final ReportTicketRepository reportTicketRepository;
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementUserRepository announcementUserRepository;
    private final AnnouncementMapper announcementMapper;
    
   public Response manageUser(int page, int size, String search, Class<? extends User> roleClass) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

            Specification<User> spec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                // Filter by role
                if (roleClass != null) {
                    predicates.add(cb.equal(root.type(), roleClass));
                }

                // Filter by search text
                if (search != null && !search.isBlank()) {
                    String pattern = "%" + search.toLowerCase() + "%";
                    predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern)
                    ));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            };

            Page<User> pageResult = userRepository.findAll(spec, pageable);

            List<UserDTO> userDTOs = pageResult.getContent()
                    .stream()
                    .map(userMapper::toUserDTO)
                    .toList();

            var pagination = Pagination.builder()
                    .content(userDTOs)
                    .page(pageResult.getNumber())
                    .size(pageResult.getSize())
                    .totalItems(pageResult.getTotalElements())
                    .totalPages(pageResult.getTotalPages())
                    .hasNext(pageResult.hasNext())
                    .hasPrevious(pageResult.hasPrevious())
                    .build();

            return Response.builder()
                    .statusCode(200)
                    .message("Users retrieved successfully")
                    .pagination(pagination)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Users retrieved failed: " + e.getMessage())
                    .build();
        }
    }

    public Response GetUserById(String id) {
        try{
            var user = userRepository.findById(Long.parseLong(id)).orElseThrow(() -> new RuntimeException("User not found"));
            return Response.builder()
                    .statusCode(200)
                    .message("User retrieved successfully")
                    .user(userMapper.toUserDTO(user))
                    .build();
        }catch(Exception e){
            return Response.builder()
                    .statusCode(400)
                    .message("User retrieved failed: " + e.getMessage())
                    .build();
        }
    }

    public Response createUser(UserDTO user) {
        Response response = new Response();
        try {
            var newUser = userMapper.toUser(user);
            userRepository.save(newUser);
            response.setMessage("User created successfully");
            response.setStatusCode(200);
            response.setUser(user);
        } catch (Exception e) {
            response.setMessage("User created failed");
            response.setStatusCode(400);
        }
        return response;
    }

    public Response getAllCourses(Integer page, Integer size, String tutorName, String status, String courseName) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "courseName"));

            CourseStatus enumStatus = null;
            if (status != null && !status.isBlank()) {
                enumStatus = CourseStatus.fromValue(status);
            }
            final CourseStatus finalStatus = enumStatus;
            Specification<Course> spec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (tutorName != null && !tutorName.isBlank()) {
                    predicates.add(cb.like(cb.lower(root.get("tutor").get("name")), "%" + tutorName.toLowerCase() + "%"));
                }

                if (finalStatus != null) {
                    predicates.add(cb.equal(root.get("courseStatus"), finalStatus));
                }

                if (courseName != null && !courseName.isBlank()) {
                    predicates.add(cb.like(cb.lower(root.get("courseName")), "%" + courseName.toLowerCase() + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            };

            Page<Course> pageResult = courseRepository.findAll(spec, pageable);

            var pagination = Pagination.builder()
                    .content(pageResult.getContent())
                    .page(pageResult.getNumber())
                    .size(pageResult.getSize())
                    .totalItems(pageResult.getTotalElements())
                    .totalPages(pageResult.getTotalPages())
                    .hasNext(pageResult.hasNext())
                    .hasPrevious(pageResult.hasPrevious())
                    .build();

            return Response.builder()
                    .statusCode(200)
                    .message("Courses retrieved successfully")
                    .pagination(pagination)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to retrieve courses: " + e.getMessage())
                    .build();
        }
    }

    public Response createCourse(Course courseDTO) {
        Response response = new Response();
        try {
            // Validate status
            CourseStatus status = (courseDTO.getCourseStatus() != null)
                    ? CourseStatus.fromValue(courseDTO.getCourseStatus().name())
                    : CourseStatus.OPEN;

            // Map DTO -> Entity
            Course course = Course.builder()
                    .courseName(courseDTO.getCourseName())
                    .maxMentee(courseDTO.getMaxMentee())
                    .startDate(courseDTO.getStartDate())
                    .endDate(courseDTO.getEndDate())
                    .courseStatus(status)
                    .build();

            courseRepository.save(course);

            response.setStatusCode(200);
            response.setMessage("Course created successfully");
        } catch (IllegalArgumentException e) {
            response.setStatusCode(400);
            response.setMessage("Invalid course status: " + courseDTO.getCourseStatus());
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to create course: " + e.getMessage());
        }
        return response;
    }





    // ==================== REPORT TICKET MANAGEMENT ====================
    
    public Response getAllReportTickets() {
        Response response = new Response();
        try {
            List<ReportTicket> tickets = reportTicketRepository.findAll();
            response.setStatusCode(200);
            response.setMessage("Report tickets retrieved successfully");
            response.setReportTicketList(tickets);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve report tickets: " + e.getMessage());
        }
        return response;
    }

    
    public Response getReportTicketById(Long ticketId) {
        Response response = new Response();
        try {
            ReportTicket ticket = reportTicketRepository.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Report ticket not found"));
            response.setStatusCode(200);
            response.setMessage("Report ticket retrieved successfully");
            response.setReportTicket(ticket);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve report ticket: " + e.getMessage());
        }
        return response;
    }

    
    public Response getReportTicketsByStatus(String status) {
        Response response = new Response();
        try {
            List<ReportTicket> tickets = reportTicketRepository.findByStatus(status);
            response.setStatusCode(200);
            response.setMessage("Report tickets retrieved successfully");
            response.setReportTicketList(tickets);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve report tickets: " + e.getMessage());
        }
        return response;
    }

    
    public Response updateReportTicketStatus(Long ticketId, String status, String adminResponse) {
        Response response = new Response();
        try {
            ReportTicket ticket = reportTicketRepository.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Report ticket not found"));

            ReportTicketStatus statuscheck = (ticket.getStatus() != null)
                    ? ReportTicketStatus.fromValue(ticket.getStatus().name())
                    : null;
            ticket.setStatus(statuscheck);
            ticket.setAdminResponse(adminResponse);
            
            if ("RESOLVED".equals(status) || "REJECTED".equals(status)) {
                ticket.setResolvedAt(LocalDateTime.now());
            }
            
            reportTicketRepository.save(ticket);
            response.setStatusCode(200);
            response.setMessage("Report ticket updated successfully");
            response.setReportTicket(ticket);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to update report ticket: " + e.getMessage());
        }
        return response;
    }

    // ==================== ANNOUNCEMENT MANAGEMENT ====================
    
    

    private Response sendAnnouncement(
            AnnouncementDTO dto,
            RecipientType type,
            Supplier<List<User>> recipientsSupplier) {
        
        try {
            Admin admin = adminRepository.findById(getCurrentUserId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            Announcement announcement = Announcement.builder()
                    .sender(admin)
                    .title(dto.getTitle())
                    .details(dto.getContent())
                    .recipientType(type.name())
                    .createdAt(LocalDateTime.now())
                    .build();

            Announcement saved = announcementRepository.save(announcement);

            List<AnnouncementUser> annUsers = recipientsSupplier.get()
                    .stream()
                    .map(u -> AnnouncementUser.builder()
                            .user(u)
                            .announcement(saved)
                            .isRead(false)
                            .build())
                    .toList();

            announcementUserRepository.saveAll(annUsers);

            return Response.builder().statusCode(200)
                    .message("Announcement sent successfully")
                    .data(announcementMapper.toDTO(saved))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to send announcement: " + e.getMessage())
                    .build();
        }
    }





    
    public Response sendAnnouncementToAll(AnnouncementDTO dto) {
        return sendAnnouncement(
                dto,
                RecipientType.ALL,
                userRepository::findAll
        );
    }



    public Response sendAnnouncementToMentee(AnnouncementDTO dto) {
        return sendAnnouncement(
                dto,
                RecipientType.MENTEE,
                userRepository::findAllMentees
        );
    }


    public Response sendAnnouncementToTutor(AnnouncementDTO dto) {
        return sendAnnouncement(
                dto,
                RecipientType.TUTOR,
                userRepository::findAllTutors
        );
    }


    public Response sendAnnouncementToUser(Long userId, AnnouncementDTO dto) {
        return sendAnnouncement(
                dto,
                RecipientType.SPECIFIC,
                () -> List.of(
                        userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"))
                )
        );
    }



    public Response getAllAnnouncements(
        Integer page, Integer size, String recipientType, String title, Long adminId
        ) {
        Response response = new Response();
        try {
            int pageNumber =  page >= 0 ? page : 0;
            int pageSize = size >= 0 ? size : 10;
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
            Specification<Announcement> spec = Specification.where(null);

            if( recipientType != null && !recipientType.isEmpty() ) {
                spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("recipientType"), recipientType)
                );
            }
            if( title != null && !title.isEmpty() ) {
                spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("title"), "%" + title + "%")
                );
            }
            if( adminId != null ) {
                spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("sender").get("id"), adminId)
                );
            }

            Page<Announcement> pageResult = announcementRepository.findAll(spec, pageable);

            Map<String, Object> data = new HashMap<>();
            data.put("content", pageResult.getContent());
            data.put("page", pageResult.getNumber());
            data.put("size", pageResult.getSize());
            data.put("totalItems", pageResult.getTotalElements());
            data.put("totalPages", pageResult.getTotalPages());
            data.put("hasNext", pageResult.hasNext());
            data.put("hasPrevious", pageResult.hasPrevious());

            response.setStatusCode(200);
            response.setMessage("Announcements retrieved successfully");
            response.setData(data);

        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve announcements: " + e.getMessage());
        }
        return response;
    }


    
    public Response getAnnouncementsByAdmin(Long adminId, int page, int size) {
        Response response = new Response();
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Announcement> pageResult = announcementRepository.findBySenderId(adminId, pageable);

            Map<String, Object> data = new HashMap<>();
            data.put("content", pageResult.getContent());
            data.put("page", pageResult.getNumber());
            data.put("size", pageResult.getSize());
            data.put("totalItems", pageResult.getTotalElements());
            data.put("totalPages", pageResult.getTotalPages());
            data.put("hasNext", pageResult.hasNext());
            data.put("hasPrevious", pageResult.hasPrevious());

            response.setStatusCode(200);
            response.setMessage("Announcements retrieved successfully");
            response.setData(data);

        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve announcements: " + e.getMessage());
        }
        return response;
    }

    public Response deleteAnnouncement(Long announcementId) {
        Response response = new Response();
        try {
            announcementRepository.deleteById(announcementId);
            response.setStatusCode(200);
            response.setMessage("Announcement deleted successfully");
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to delete announcement: " + e.getMessage());
        }
        return response;
    }

    /**
     * Lấy userId từ SecurityContext (JWT token)
     */
    private Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
            return Long.parseLong(userDetails.getUserId());
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

}
