package com.example.lms.service.imple;

import com.example.lms.dto.*;
import com.example.lms.enums.*;
import com.example.lms.mapper.ReportTicketMapper;
import com.example.lms.model.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import com.example.lms.security.JwtUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.lms.mapper.AnnouncementMapper;
import com.example.lms.mapper.UserMapper;
import com.example.lms.mapper.CourseMapper;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class AdminServiceImple implements AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final MenteeRepository menteeRepository;
    private final UserMapper userMapper;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final SubjectRegistrationRepository subjectRegistrationRepository;
    private final ReportTicketRepository reportTicketRepository;
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementUserRepository announcementUserRepository;
    private final AnnouncementMapper announcementMapper;
    private final ReportTicketMapper reportTicketMapper;
    private final EnrollmentRepository enrollmentRepository;
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

            var courseDTOs = pageResult.getContent().stream()
                    .map(courseMapper::toDTO)
                    .toList();
            
            var pagination = Pagination.builder()
                    .content(courseDTOs)
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

    public Response createCourse(CourseDTO courseDTO) {
        Response response = new Response();
        try {
            if (courseDTO.getSubjectRegistrationId() == null ) {
                response.setStatusCode(400);
                response.setMessage("SubjectRegistration ID is required");
                return response;
            }

            SubjectRegistration subjectRegistration = subjectRegistrationRepository
                    .findById(courseDTO.getSubjectRegistrationId())
                    .orElse(null);

            if (subjectRegistration == null) {
                response.setStatusCode(400);
                response.setMessage("SubjectRegistration not found with ID: " + courseDTO.getSubjectRegistrationId());
                return response;
            }

            CourseStatus status = (courseDTO.getCourseStatus() != null)
                    ? CourseStatus.fromValue(courseDTO.getCourseStatus())
                    : CourseStatus.OPEN;
            
            var courseName = subjectRegistration.getSubject().getSubjectName();
            subjectRegistration.setRegistrationStatus(RegistrationStatus.APPROVED);
            // Map DTO -> Entity
            Course course = Course.builder()
                    .courseName(courseName)
                    .description(courseDTO.getDescription())
                    .maxMentee(courseDTO.getMaxMentee())
                    .startDate(courseDTO.getStartDate())
                    .endDate(courseDTO.getEndDate())
                    .createdDate(LocalDateTime.now())
                    .courseStatus(status)
                    .subjectRegistration(subjectRegistration)
                    .build();

            courseRepository.save(course);
            subjectRegistrationRepository.save(subjectRegistration);


            response.setStatusCode(201);
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

    @Override
    public Response getCourseById(Long courseId) {
        Response response = new Response();
        try {
            Course course = courseRepository.findById(courseId)
                    .orElse(null);

            if (course == null) {
                response.setStatusCode(404);
                response.setMessage("Course not found with ID: " + courseId);
                return response;
            }

            CourseDTO dto = courseMapper.toDTO(course);

            response.setStatusCode(200);
            response.setMessage("Course retrieved successfully");
            response.setData(dto);
            return response;

        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve course: " + e.getMessage());
            return response;
        }
    }
    @Override
    public Response updateCourse(Long courseId, CourseDTO dto) {
        Response response = new Response();
        try {
            Course course = courseRepository.findById(courseId)
                    .orElse(null);

            if (course == null) {
                response.setStatusCode(404);
                response.setMessage("Course not found with ID: " + courseId);
                return response;
            }

            // Nếu sửa subjectRegistrationId
            if (dto.getSubjectRegistrationId() != null) {
                SubjectRegistration reg = subjectRegistrationRepository
                        .findById(dto.getSubjectRegistrationId())
                        .orElse(null);

                if (reg == null) {
                    response.setStatusCode(400);
                    response.setMessage("SubjectRegistration not found with ID: "
                            + dto.getSubjectRegistrationId());
                    return response;
                }

                course.setSubjectRegistration(reg);

                // CourseName = subjectName theo logic bạn
                course.setCourseName(reg.getSubject().getSubjectName());
            }

            // Update các field còn lại
            if (dto.getDescription() != null)
                course.setDescription(dto.getDescription());

            if (dto.getMaxMentee() != null)
                course.setMaxMentee(dto.getMaxMentee());

            if (dto.getStartDate() != null)
                course.setStartDate(dto.getStartDate());

            if (dto.getEndDate() != null)
                course.setEndDate(dto.getEndDate());

            if (dto.getCourseStatus() != null) {
                CourseStatus status = CourseStatus.fromValue(dto.getCourseStatus());
                course.setCourseStatus(status);
            }


            courseRepository.save(course);

            response.setStatusCode(200);
            response.setMessage("Course updated successfully");
            response.setData(courseMapper.toDTO(course));

            return response;

        } catch (IllegalArgumentException e) {
            response.setStatusCode(400);
            response.setMessage("Invalid course status: " + dto.getCourseStatus());
            return response;

        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to update course: " + e.getMessage());
            return response;
        }
    }





    // ==================== REPORT TICKET MANAGEMENT ====================

    public Response createReportTicket(ReportTicketDTO reportTicket) {
        try {

            Long menteeId = getCurrentUserId();
            Mentee mentee = menteeRepository.findMenteeById(menteeId);

            ReportTicket rp = new ReportTicket();
            rp.setMentee(mentee);
            rp.setTitle(reportTicket.getTitle());
            rp.setDescription(reportTicket.getDescription());
            rp.setAdminResponse(null);
            rp.setResolvedBy(null);

            var saved = reportTicketRepository.save(rp);

            return Response.builder()
                    .statusCode(200)
                    .message("Report ticket created successfully")
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to create report ticket: " + e.getMessage())
                    .build();
        }
    }


    public Response deleteReportTicket(Long ticketId) {
        Response response = new Response();
        try {
            reportTicketRepository.deleteById(ticketId);
            response.setStatusCode(200);
            response.setMessage("Report ticket deleted successfully");
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to delete report ticket: " + e.getMessage());
        }
        return response;
    }
    public Response getAllReportTickets(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ReportTicket> pageResult = reportTicketRepository.findAll(pageable);

            List<ReportTicketDTO> dto = pageResult.getContent()
                    .stream()
                    .map(reportTicketMapper::toDTO)
                    .toList();

            var pagination = Pagination.builder()
                    .content(dto)
                    .page(pageResult.getNumber())
                    .size(pageResult.getSize())
                    .totalItems(pageResult.getTotalElements())
                    .totalPages(pageResult.getTotalPages())
                    .hasNext(pageResult.hasNext())
                    .hasPrevious(pageResult.hasPrevious())
                    .build();
            

            return Response.builder()
                    .statusCode(200)
                    .pagination(pagination)
                    .message("Report tickets retrieved successfully")
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to retrieve report tickets: " + e.getMessage())
                    .build();
        }
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

        // Convert string -> enum
        ReportTicketStatus newStatus;
        try {
            newStatus = ReportTicketStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + status);
        }

        // Set new status
        ticket.setStatus(newStatus);

        // Set admin response
        ticket.setAdminResponse(adminResponse);

        // Auto set resolved time
        if (newStatus == ReportTicketStatus.APPROVED || newStatus == ReportTicketStatus.REJECTED) {
            ticket.setResolvedAt(LocalDateTime.now());
        }

        reportTicketRepository.save(ticket);

        response.setStatusCode(200);
        response.setMessage("Report ticket updated successfully");
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
        try {
            int pageNumber = (page != null && page >= 0) ? page : 0;
            int pageSize = (size != null && size > 0) ? size : 10;
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

            Specification<Announcement> spec = Specification.where(null);

            if (recipientType != null && !recipientType.isBlank()) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("recipientType"), recipientType));
            }
            if (title != null && !title.isBlank()) {
                spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (adminId != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("sender").get("id"), adminId));
            }

            Page<Announcement> pageResult = announcementRepository.findAll(spec, pageable);
            
            // Convert to DTOs to avoid circular references
            var announcementDTOs = pageResult.getContent().stream()
                    .map(announcementMapper::toDTO)
                    .toList();

            var pagination = Pagination.builder()
                    .content(announcementDTOs)
                    .page(pageResult.getNumber())
                    .size(pageResult.getSize())
                    .totalItems(pageResult.getTotalElements())
                    .totalPages(pageResult.getTotalPages())
                    .hasNext(pageResult.hasNext())
                    .hasPrevious(pageResult.hasPrevious())
                    .build();

            return Response.builder()
                    .statusCode(200)
                    .message("Announcements retrieved successfully")
                    .pagination(pagination)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to retrieve announcements: " + e.getMessage())
                    .build();
        }
    }



    
    public Response getAnnouncementsByAdmin(Long adminId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Announcement> pageResult = announcementRepository.findBySenderId(adminId, pageable);
            
            // Convert to DTOs to avoid circular references
            var announcementDTOs = pageResult.getContent().stream()
                    .map(announcementMapper::toDTO)
                    .toList();
            
            var pagination = Pagination.builder()
                    .content(announcementDTOs)
                    .page(pageResult.getNumber())
                    .size(pageResult.getSize())
                    .totalItems(pageResult.getTotalElements())
                    .totalPages(pageResult.getTotalPages())
                    .hasNext(pageResult.hasNext())
                    .hasPrevious(pageResult.hasPrevious())
                    .build();
            return Response.builder()
                    .statusCode(200)
                    .message("Announcements retrieved successfully")
                    .pagination(pagination)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to retrieve announcements: " + e.getMessage())
                    .build();
        }
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

    public Response getAllsubjectRegistration() {
        try {
            List<SubjectRegistration> all = subjectRegistrationRepository
                    .findAll(Sort.by(Sort.Direction.DESC, "registeredAt"));

            // Lọc chỉ lấy PENDING
            List<SubjectRegistration> pendingOnly = all.stream()
                    .filter(sr -> sr.getRegistrationStatus() == RegistrationStatus.PENDING)
                    .toList();

            return Response.builder()
                    .statusCode(200)
                    .data(pendingOnly)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to retrieve subjectRegistrations: " + e.getMessage())
                    .build();
        }
    }


    @Override
    public Response getEnrollmentStats(Long courseId) {
        try {
            var all = enrollmentRepository.findByCourse_CourseId(courseId);

            long approved = all.stream()
                    .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                    .count();

            long pending = all.stream()
                    .filter(e -> e.getStatus() == EnrollmentStatus.PENDING)
                    .count();

            return Response.builder().statusCode(200).data(Map.of(
                    "approved", approved,
                    "pending", pending,
                    "total", approved + pending
            )).build();
        } catch (Exception e) {
            return Response.builder().statusCode(400).message("Failed to retrieve enrollments: " + e.getMessage()).build();
        }
    }

    @Override
    public Response approveByAvailableSlots(Long courseId) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            var maxMentee = course.getMaxMentee();

            List<Enrollment> all = enrollmentRepository.findByCourse_CourseId(courseId);

            long approvedCount = all.stream()
                    .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                    .count();

            Long slots = maxMentee - (Long) approvedCount;

            if (slots <= 0) {
                return Response.builder().statusCode(200).message("no available slot").build();
            }

            // Lấy danh sách pending và sort theo thời gian đăng ký
            List<Enrollment> toApprove = all.stream()
                    .filter(e -> e.getStatus() == EnrollmentStatus.PENDING)
                    .sorted(Comparator.comparing(Enrollment::getEnrolledAt)) // FIFO
                    .limit(slots)
                    .toList();

            if (toApprove.isEmpty()) {
                return Response.builder().statusCode(200).message("No pending enrollments to approve").build();
            }

            // Approve
            toApprove.forEach(e -> e.setStatus(EnrollmentStatus.ACTIVE));
            enrollmentRepository.saveAll(toApprove);

            return Response.builder().statusCode(200).data(Map.of(
                    "approvedNow", toApprove.size(),
                    "totalApproved", approvedCount + toApprove.size(),
                    "slotsRemaining", maxMentee - (approvedCount + toApprove.size())
            )).build();

        } catch (Exception e) {
            return Response.builder().statusCode(400).message("Failed to retrieve enrollments: " + e.getMessage()).build();
        }
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
