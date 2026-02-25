package com.example.lms.service.interf;


import com.example.lms.dto.*;
import com.example.lms.model.User;

public interface AdminService {
    //user management
    Response manageUser(int page, int size, String search, Class<? extends User> role);
    Response GetUserById(String id);
    Response createUser(UserDTO user);
    //course management
    Response getAllCourses(Integer page, Integer size, String tutor, String status, String course_name);
    Response createCourse(CourseDTO courseDTO);
    Response updateCourse(Long courseId, CourseDTO dto);
    Response getCourseById(Long courseId);
    // Report Ticket Management
    Response createReportTicket(ReportTicketDTO reportTicket);
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



    // registration

    Response getAllsubjectRegistration();

    //enroll
    Response approveByAvailableSlots(Long courseId);
    Response getEnrollmentStats(Long courseId);
}
