package com.example.lms.service.interf;


import com.example.lms.dto.Response;
import com.example.lms.dto.UserDTO;
import com.example.lms.dto.AnnouncementDTO;
import com.example.lms.model.Course;

public interface AdminService {
    Response ManageUser(int page, int size);
    Response GetUserById(String id);
    Response createUser(UserDTO user);
    Response getAllCourses(int page);
    Response createCourse(Course course);

    // Report Ticket Management
    Response getAllReportTickets();
    Response getReportTicketById(Long ticketId);
    Response getReportTicketsByStatus(String status);
    Response updateReportTicketStatus(Long ticketId, String status, String adminResponse);

    // Announcement Management
    Response sendAnnouncementToAll(AnnouncementDTO announcement);
    Response sendAnnouncementToMentee(AnnouncementDTO announcement);
    Response sendAnnouncementToTutor(AnnouncementDTO announcement);
    Response sendAnnouncementToUser(Long userId, AnnouncementDTO announcement);
    Response getAllAnnouncements(int page, int size);
    Response getAnnouncementsByAdmin(Long adminId, int page, int size);
    Response deleteAnnouncement(Long announcementId);
}
