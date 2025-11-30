package com.example.lms.service.interf;


import com.example.lms.dto.CourseDTO;
import com.example.lms.dto.Response;
import com.example.lms.dto.UserDTO;
import com.example.lms.dto.AnnouncementDTO;
import com.example.lms.model.Course;
import com.example.lms.model.ReportTicket;
import com.example.lms.model.User;

public interface AdminService {
    //user management
    Response manageUser(int page, int size, String search, Class<? extends User> role);
    Response GetUserById(String id);
    Response createUser(UserDTO user);
    //course management
    Response getAllCourses(Integer page, Integer size, String tutor, String status, String course_name);
    Response createCourse(CourseDTO courseDTO);

    // Report Ticket Management
    Response createReportTicket(ReportTicket reportTicket);
    Response deleteReportTicket(Long ticketId);
    Response getAllReportTickets(int page, int size);
    Response getReportTicketById(Long ticketId);
    Response getReportTicketsByStatus(String status);
    Response updateReportTicketStatus(Long ticketId, String status, String adminResponse);

    // Announcement Management
    Response sendAnnouncementToAll(AnnouncementDTO announcement);
    Response sendAnnouncementToMentee(AnnouncementDTO announcement);
    Response sendAnnouncementToTutor(AnnouncementDTO announcement);
    Response sendAnnouncementToUser(Long userId, AnnouncementDTO announcement);
    Response getAllAnnouncements(Integer page, Integer size, String recipientType, String title, Long adminId);
    Response getAnnouncementsByAdmin(Long adminId, int page, int size);
    Response deleteAnnouncement(Long announcementId);
}
