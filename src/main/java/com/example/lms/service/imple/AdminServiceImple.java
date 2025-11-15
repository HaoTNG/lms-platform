package com.example.lms.service.imple;

import com.example.lms.Mapper.UserMapper;
import com.example.lms.dto.Response;
import com.example.lms.dto.UserDTO;
import com.example.lms.dto.AnnouncementDTO;
import com.example.lms.model.Course;
import com.example.lms.model.Announcement;
import com.example.lms.model.ReportTicket;
import com.example.lms.model.User;
import com.example.lms.model.Admin;
import com.example.lms.repository.*;
import com.example.lms.service.interf.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminServiceImple implements AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CourseRepository courseRepository;
    private final ReportTicketRepository reportTicketRepository;
    private final AnnouncementRepository announcementRepository;

    public Response ManageUser() {
        Response response = new Response();
        try{
            response.setUserList(userRepository.findAll().stream().map(userMapper::toUserDTO).toList());
            response.setMessage("Success");
            response.setStatusCode(200);
        }catch(Exception e){
            response.setMessage("Users retrieved failed");
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

    public Response getAllCourses(int page) {
        Response response = new Response();
        try {
            var pageable = PageRequest.of(page, 10); // phân trang 10 course
            var courses = courseRepository.findAll(pageable).getContent();
            response.setStatusCode(200);
            response.setMessage("Courses retrieved successfully");
            response.setUserList(null);
            response.setMenteeList(null);
            response.setTutorLIst(null);
            response.setUser(null);
            response.setCourseList(courses);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve courses");
        }
        return response;
    }

    public Response createCourse(Course course) {
        Response response = new Response();
        try {
            courseRepository.save(course);
            response.setStatusCode(200);
            response.setMessage("Course created successfully");
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to create course");
        }
        return response;
    }

    // ==================== REPORT TICKET MANAGEMENT ====================
    @Override
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

    @Override
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

    @Override
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

    @Override
    public Response updateReportTicketStatus(Long ticketId, String status, String adminResponse) {
        Response response = new Response();
        try {
            ReportTicket ticket = reportTicketRepository.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Report ticket not found"));
            
            ticket.setStatus(status);
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
    @Override
    public Response sendAnnouncementToAll(AnnouncementDTO announcementDTO) {
        Response response = new Response();
        try {
            Announcement announcement = new Announcement();
            announcement.setTitle(announcementDTO.getTitle());
            announcement.setContent(announcementDTO.getContent());
            announcement.setRecipientType("ALL");
            announcement.setCreatedAt(LocalDateTime.now());
            announcement.setIsRead(false);
            
            // Set admin nếu có
            if (announcementDTO.getAdminId() != null) {
                Admin admin = adminRepository.findById(announcementDTO.getAdminId())
                        .orElseThrow(() -> new RuntimeException("Admin not found"));
                announcement.setAdmin(admin);
            }
            
            announcementRepository.save(announcement);
            response.setStatusCode(200);
            response.setMessage("Announcement sent to all users successfully");
            response.setAnnouncement(announcement);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to send announcement: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response sendAnnouncementToMentee(AnnouncementDTO announcementDTO) {
        Response response = new Response();
        try {
            // Lấy tất cả các mentee
            List<User> mentees = userRepository.findAll()
                    .stream()
                    .filter(user -> "MENTEE".equals(user.getRole()))
                    .toList();

            List<Announcement> announcements = new ArrayList<>();
            for (User mentee : mentees) {
                Announcement announcement = new Announcement();
                announcement.setTitle(announcementDTO.getTitle());
                announcement.setContent(announcementDTO.getContent());
                announcement.setRecipientType("MENTEE");
                announcement.setRecipientUser(mentee);
                announcement.setCreatedAt(LocalDateTime.now());
                announcement.setIsRead(false);
                
                if (announcementDTO.getAdminId() != null) {
                    Admin admin = adminRepository.findById(announcementDTO.getAdminId())
                            .orElseThrow(() -> new RuntimeException("Admin not found"));
                    announcement.setAdmin(admin);
                }
                
                announcements.add(announcement);
            }
            
            announcementRepository.saveAll(announcements);
            response.setStatusCode(200);
            response.setMessage("Announcement sent to all mentees successfully");
            response.setAnnouncementList(announcements);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to send announcement: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response sendAnnouncementToTutor(AnnouncementDTO announcementDTO) {
        Response response = new Response();
        try {
            // Lấy tất cả các tutor
            List<User> tutors = userRepository.findAll()
                    .stream()
                    .filter(user -> "TUTOR".equals(user.getRole()))
                    .toList();

            List<Announcement> announcements = new ArrayList<>();
            for (User tutor : tutors) {
                Announcement announcement = new Announcement();
                announcement.setTitle(announcementDTO.getTitle());
                announcement.setContent(announcementDTO.getContent());
                announcement.setRecipientType("TUTOR");
                announcement.setRecipientUser(tutor);
                announcement.setCreatedAt(LocalDateTime.now());
                announcement.setIsRead(false);
                
                if (announcementDTO.getAdminId() != null) {
                    Admin admin = adminRepository.findById(announcementDTO.getAdminId())
                            .orElseThrow(() -> new RuntimeException("Admin not found"));
                    announcement.setAdmin(admin);
                }
                
                announcements.add(announcement);
            }
            
            announcementRepository.saveAll(announcements);
            response.setStatusCode(200);
            response.setMessage("Announcement sent to all tutors successfully");
            response.setAnnouncementList(announcements);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to send announcement: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response sendAnnouncementToUser(Long userId, AnnouncementDTO announcementDTO) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Announcement announcement = new Announcement();
            announcement.setTitle(announcementDTO.getTitle());
            announcement.setContent(announcementDTO.getContent());
            announcement.setRecipientType("SPECIFIC");
            announcement.setRecipientUser(user);
            announcement.setCreatedAt(LocalDateTime.now());
            announcement.setIsRead(false);
            
            if (announcementDTO.getAdminId() != null) {
                Admin admin = adminRepository.findById(announcementDTO.getAdminId())
                        .orElseThrow(() -> new RuntimeException("Admin not found"));
                announcement.setAdmin(admin);
            }
            
            announcementRepository.save(announcement);
            response.setStatusCode(200);
            response.setMessage("Announcement sent to user successfully");
            response.setAnnouncement(announcement);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to send announcement: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAnnouncements() {
        Response response = new Response();
        try {
            List<Announcement> announcements = announcementRepository.findAll();
            response.setStatusCode(200);
            response.setMessage("Announcements retrieved successfully");
            response.setAnnouncementList(announcements);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve announcements: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAnnouncementsByAdmin(Long adminId) {
        Response response = new Response();
        try {
            List<Announcement> announcements = announcementRepository.findByAdminId(adminId);
            response.setStatusCode(200);
            response.setMessage("Announcements retrieved successfully");
            response.setAnnouncementList(announcements);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve announcements: " + e.getMessage());
        }
        return response;
    }

    @Override
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
