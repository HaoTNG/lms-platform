package com.example.lms.service.imple;

import com.example.lms.enums.CourseStatus;
import com.example.lms.enums.ReportTicketStatus;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import com.example.lms.dto.Response;
import com.example.lms.dto.UserDTO;
import com.example.lms.mapper.AnnouncementMapper;
import com.example.lms.mapper.UserMapper;
import com.example.lms.dto.AnnouncementDTO;
import com.example.lms.model.Course;
import com.example.lms.model.Announcement;
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


@Service
@RequiredArgsConstructor
public class AdminServiceImple implements AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CourseRepository courseRepository;
    private final ReportTicketRepository reportTicketRepository;
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementMapper announcementMapper;
    public Response ManageUser(int page, int size) {
        Response response = new Response();
    try {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").ascending());
        Page<User> pageResult = userRepository.findAll(pageable);

        List<UserDTO> userDTOs = pageResult.getContent()
                .stream()
                .map(userMapper::toUserDTO)
                .toList();




        // Thêm info phân trang
        Map<String, Object> data = new HashMap<>();
        data.put("content", userDTOs);
        data.put("page", pageResult.getNumber());
        data.put("size", pageResult.getSize());
        data.put("totalItems", pageResult.getTotalElements());
        data.put("totalPages", pageResult.getTotalPages());
        data.put("hasNext", pageResult.hasNext());
        data.put("hasPrevious", pageResult.hasPrevious());
        response.setData(data);
        response.setMessage("Users retrieved successfully");
        response.setStatusCode(200);
    } catch (Exception e) {
        response.setMessage("Users retrieved failed: " + e.getMessage());
        response.setStatusCode(400);
    }
    return response;
    }

    public Response GetUserById(String id) {
        Response response = new Response();
        try{
            var user = userRepository.findById(Long.parseLong(id)).orElseThrow(() -> new RuntimeException("User not found"));
            response.setUser(userMapper.toUserDTO(user));
            response.setMessage("Success");
            response.setStatusCode(200);
        }catch(Exception e){
            response.setMessage("Users retrieved failed");
            response.setStatusCode(400);
        }
        return response;
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

    public Response getAllCourses(Integer page, Integer size, String tutor, String status, String course_name) {
        Response response = new Response();
        try {
            var pageable = PageRequest.of(page, size);

            CourseStatus enumStatus;
            if (status != null && !status.isEmpty()) {
                enumStatus = CourseStatus.fromValue(status); // chỉ chấp nhận OPEN hoặc END
            } else {
                enumStatus = null;
            }

            ;
            Specification<Course> spec = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (tutor != null && !tutor.isEmpty()) {
                    predicates.add(criteriaBuilder.equal(root.get("tutor"), tutor));
                }
                if (status != null && !status.isEmpty()) {
                    predicates.add(criteriaBuilder.equal(root.get("course_status"), enumStatus));
                }
                if (course_name != null && !course_name.isEmpty()) {
                    predicates.add(criteriaBuilder.like(root.get("course_name"), "%" + course_name + "%"));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            Page<Course> pageResult = courseRepository.findAll(spec, pageable);
            
             
            

            Map<String, Object> data = new HashMap<>();
            data.put("content", pageResult.getContent());
            data.put("page", pageResult.getNumber());
            data.put("size", pageResult.getSize());
            data.put("totalItems", pageResult.getTotalElements());
            data.put("totalPages", pageResult.getTotalPages());
            data.put("hasNext", pageResult.hasNext());
            data.put("hasPrevious", pageResult.hasPrevious());
            response.setStatusCode(200);
            response.setMessage("Courses retrieved successfully");
            response.setData(data);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve courses: " + e.getMessage());
        }
        return response;
    }

    public Response createCourse(Course courseDTO) {
        Response response = new Response();
        try {
            // Validate status
            CourseStatus status = (courseDTO.getCourse_status() != null)
                    ? CourseStatus.fromValue(courseDTO.getCourse_status().name())
                    : CourseStatus.OPEN;

            // Map DTO -> Entity
            Course course = Course.builder()
                    .course_name(courseDTO.getCourse_name())
                    .tutor(courseDTO.getTutor())
                    .max_no_mentee(courseDTO.getMax_no_mentee())
                    .start_date(courseDTO.getStart_date())
                    .end_date(courseDTO.getEnd_date())
                    .course_status(status)
                    .build();

            courseRepository.save(course);

            response.setStatusCode(200);
            response.setMessage("Course created successfully");
        } catch (IllegalArgumentException e) {
            response.setStatusCode(400);
            response.setMessage("Invalid course status: " + courseDTO.getCourse_status());
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
    
    public Response sendAnnouncementToAll(AnnouncementDTO announcementDTO) {
        Response response = new Response();

        try {
            Admin admin = adminRepository.findById(announcementDTO.getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            
            Announcement announcement = announcementMapper.toEntity(announcementDTO);
            announcement.setAdmin(admin);
            announcement.setRecipientType("ALL");
            announcement.setCreatedAt(LocalDateTime.now());
            announcement.setIsRead(false);

            
            Announcement saved = announcementRepository.save(announcement);

            
            AnnouncementDTO dto = announcementMapper.toDTO(saved);

            
            response.setStatusCode(200);
            response.setMessage("Announcement sent to all users successfully");
            response.setAnnouncementDTO(dto);

        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to send announcement: " + e.getMessage());
        }

        return response;
    }


    
    public Response sendAnnouncementToMentee(AnnouncementDTO announcementDTO) {
    Response response = new Response();

    try {
        // Lấy mentee
        List<User> mentees = userRepository.findAll()
                .stream()
                .filter(u -> "MENTEE".equals(u.getRole()))
                .toList();

        Admin admin = null;
        if (announcementDTO.getAdminId() != null) {
            admin = adminRepository.findById(announcementDTO.getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
        }

        // Tạo list entity
        List<Announcement> entities = new ArrayList<>();
        for (User mentee : mentees) {
            Announcement a = announcementMapper.toEntity(announcementDTO);
            a.setAdmin(admin);
            a.setRecipientUser(mentee);
            a.setRecipientType("MENTEE");
            a.setCreatedAt(LocalDateTime.now());
            a.setIsRead(false);
            entities.add(a);
        }

        // Save all
        List<Announcement> saved = announcementRepository.saveAll(entities);

        // Convert to DTO list
        List<AnnouncementDTO> dtos = saved.stream()
                .map(announcementMapper::toDTO)
                .toList();

        response.setStatusCode(200);
        response.setMessage("Announcement sent to all mentees successfully");
        response.setAnnouncementListDTO(dtos);

    } catch (Exception e) {
        response.setStatusCode(400);
        response.setMessage("Failed to send announcement: " + e.getMessage());
    }

    return response;
}


    
    public Response sendAnnouncementToTutor(AnnouncementDTO announcementDTO) {
        Response response = new Response();

        try {
            List<User> tutors = userRepository.findAll()
                    .stream()
                    .filter(u -> "TUTOR".equals(u.getRole()))
                    .toList();

            Admin admin = null;
            if (announcementDTO.getAdminId() != null) {
                admin = adminRepository.findById(announcementDTO.getAdminId())
                        .orElseThrow(() -> new RuntimeException("Admin not found"));
            }

            List<Announcement> entities = new ArrayList<>();
            for (User tutor : tutors) {
                Announcement a = announcementMapper.toEntity(announcementDTO);
                a.setAdmin(admin);
                a.setRecipientUser(tutor);
                a.setRecipientType("TUTOR");
                a.setCreatedAt(LocalDateTime.now());
                a.setIsRead(false);

                entities.add(a);
            }

            List<Announcement> saved = announcementRepository.saveAll(entities);

            List<AnnouncementDTO> dtos = saved.stream()
                    .map(announcementMapper::toDTO)
                    .toList();

            response.setStatusCode(200);
            response.setMessage("Announcement sent to all tutors successfully");
            response.setAnnouncementListDTO(dtos);

        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to send announcement: " + e.getMessage());
        }

        return response;
    }



    
    public Response sendAnnouncementToUser(Long userId, AnnouncementDTO announcementDTO) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Admin admin = null;
            if (announcementDTO.getAdminId() != null) {
                admin = adminRepository.findById(announcementDTO.getAdminId())
                        .orElseThrow(() -> new RuntimeException("Admin not found"));
            }

            Announcement a = announcementMapper.toEntity(announcementDTO);

            a.setRecipientUser(user);
            a.setRecipientType("SPECIFIC");
            a.setCreatedAt(LocalDateTime.now());
            a.setIsRead(false);
            a.setAdmin(admin);

            Announcement saved = announcementRepository.save(a);
            AnnouncementDTO dto = announcementMapper.toDTO(saved);

            response.setStatusCode(200);
            response.setMessage("Announcement sent to user successfully");
            response.setAnnouncementDTO(dto);

        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to send announcement: " + e.getMessage());
        }

        return response;
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
                criteriaBuilder.equal(root.get("admin").get("id"), adminId)
            );
        }

        Page<Announcement> pageResult = announcementRepository.findAll(spec, pageable);

        List<AnnouncementDTO> dtos = pageResult.getContent()
                .stream()
                .map(announcementMapper::toDTO)
                .toList();

        Map<String, Object> data = new HashMap<>();
        data.put("content", dtos);
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
        Page<Announcement> pageResult = announcementRepository.findByAdminId(adminId, pageable);

        List<AnnouncementDTO> dtos = pageResult.getContent()
                .stream()
                .map(announcementMapper::toDTO)
                .toList();

        Map<String, Object> data = new HashMap<>();
        data.put("content", dtos);
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

}
