package com.example.lms.controller;



import com.example.lms.dto.*;
import com.example.lms.model.*;
import com.example.lms.service.interf.AdminService;
import com.example.lms.service.interf.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final AdminAnalyticsService adminAnalyticsService;




    




    // ==================== USER MANAGEMENT ====================
    @GetMapping("/manage-user")
    public ResponseEntity<Response> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role // "MENTEE", "TUTOR", "ADMIN"
    ) {
        Class<? extends User> roleClass = switch (role != null ? role.toUpperCase() : "") {
            case "MENTEE" -> Mentee.class;
            case "TUTOR" -> Tutor.class;
            case "ADMIN" -> Admin.class;
            default -> null; // null = tất cả user
        };

        Response response = adminService.manageUser(page, size, search, roleClass);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/create-user")
    public ResponseEntity<Response> createUser(@RequestBody UserDTO user) {
        Response response = adminService.createUser(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable("id") String id) {
        Response response = adminService.GetUserById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ==================== COURSE MANAGEMENT ====================
    @GetMapping("/courses")
    public ResponseEntity<Response> getAllCourses(
        @RequestParam(name = "page",defaultValue = "0") int page,
        @RequestParam(name = "size",defaultValue = "10") int size,
        @RequestParam(required = false) String tutor,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String course_name
        ) {
        Response response = adminService.getAllCourses(page, size, tutor, status, course_name  );
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/courses")
    public ResponseEntity<Response> createCourse(@RequestBody CourseDTO course) {
        Response response = adminService.createCourse(course);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Response> getCourseById(@PathVariable("courseId") Long courseId) {
        Response response = adminService.getCourseById(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/courses/{courseId}")
    public ResponseEntity<Response> updateCourse(
            @PathVariable("courseId") Long courseId,
            @RequestBody CourseDTO courseDTO) {
        Response response = adminService.updateCourse(courseId,courseDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ==================== REPORT TICKET MANAGEMENT ====================
    @PostMapping("/report-tickets")
    public ResponseEntity<Response> createReportTicket(@RequestBody ReportTicketDTO reportTicket) {
        Response response = adminService.createReportTicket(reportTicket);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
    @DeleteMapping("/report-tickets/{id}")
    public ResponseEntity<Response> deleteReportTicket(@PathVariable("id") Long ticketId) {
        Response response = adminService.deleteReportTicket(ticketId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/report-tickets")
    public ResponseEntity<Response> getAllReportTickets(
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "10") int size
    ) {
        Response response = adminService.getAllReportTickets(page, size);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/report-tickets/{id}")
    public ResponseEntity<Response> getReportTicketById(@PathVariable("id") Long ticketId) {
        Response response = adminService.getReportTicketById(ticketId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/report-tickets/status/{status}")
    public ResponseEntity<Response> getReportTicketsByStatus(@PathVariable("status") String status) {
        Response response = adminService.getReportTicketsByStatus(status);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/report-tickets/{id}")
    public ResponseEntity<Response> updateReportTicketStatus(
            @PathVariable("id") Long ticketId,
            @RequestParam String status,
            @RequestParam(required = false) String adminResponse) {
        Response response = adminService.updateReportTicketStatus(ticketId, status, adminResponse);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ==================== ANNOUNCEMENT MANAGEMENT ====================
    // Gửi thông báo cho tất cả người dùng
    @PostMapping("/announcements/send-all")
    public ResponseEntity<Response> sendAnnouncementToAll(@RequestBody AnnouncementDTO announcement) {
        Response response = adminService.sendAnnouncementToAll(announcement);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Gửi thông báo cho tất cả mentee
    @PostMapping("/announcements/send-mentee")
    public ResponseEntity<Response> sendAnnouncementToMentee(@RequestBody AnnouncementDTO announcement) {
        Response response = adminService.sendAnnouncementToMentee(announcement);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Gửi thông báo cho tất cả tutor
    @PostMapping("/announcements/send-tutor")
    public ResponseEntity<Response> sendAnnouncementToTutor(@RequestBody AnnouncementDTO announcement) {
        Response response = adminService.sendAnnouncementToTutor(announcement);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Gửi thông báo cho một người dùng cụ thể
    @PostMapping("/announcements/send-user/{userId}")
    public ResponseEntity<Response> sendAnnouncementToUser(
            @PathVariable("userId") Long userId,
            @RequestBody AnnouncementDTO announcement) {
        Response response = adminService.sendAnnouncementToUser(userId, announcement);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Lấy tất cả thông báo
    @GetMapping("/announcements")
    public ResponseEntity<Response> getAllAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String recipientType,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long adminId
    ) {
        Response response = adminService.getAllAnnouncements(page, size, recipientType, title, adminId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    // Lấy tất cả thông báo của một admin
    @GetMapping("/announcements/admin/{adminId}")
    public ResponseEntity<Response> getAnnouncementsByAdmin(
            @PathVariable Long adminId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Response response = adminService.getAnnouncementsByAdmin(adminId, page, size);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    // Xóa thông báo
    @DeleteMapping("/announcements/{announcementId}")
    public ResponseEntity<Response> deleteAnnouncement(@PathVariable("announcementId") Long announcementId) {
        Response response = adminService.deleteAnnouncement(announcementId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ==================== ANALYTICS ====================
    // Lấy tất cả analytics
    @GetMapping("/analytics")
    public ResponseEntity<Response> getAllAnalytics() {
        Response response = adminAnalyticsService.getAllAnalytics();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Lấy thống kê hệ thống
    @GetMapping("/analytics/system")
    public ResponseEntity<Response> getSystemStatistics() {
        AdminAnalyticsDTO.SystemStatsDTO stats = adminAnalyticsService.getSystemStatistics();
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .message("System statistics retrieved successfully")
                .data(stats)
                .build());
    }

    // Lấy analytics sinh viên
    @GetMapping("/analytics/students")
    public ResponseEntity<Response> getStudentAnalytics() {
        AdminAnalyticsDTO.StudentAnalyticsDTO analytics = adminAnalyticsService.getStudentAnalytics();
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .message("Student analytics retrieved successfully")
                .data(analytics)
                .build());
    }

    // Lấy analytics giáo viên
    @GetMapping("/analytics/tutors")
    public ResponseEntity<Response> getTutorAnalytics() {
        AdminAnalyticsDTO.TutorAnalyticsDTO analytics = adminAnalyticsService.getTutorAnalytics();
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .message("Tutor analytics retrieved successfully")
                .data(analytics)
                .build());
    }


    @GetMapping("/subject-registrations")
    public ResponseEntity<Response> getAllSubjectRegistrations() {
        Response response = adminService.getAllsubjectRegistration();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("/courses/{courseId}/enrollments/stats")
    public ResponseEntity<Response> getEnrollmentStats(@PathVariable Long courseId) {
        var response = adminService.getEnrollmentStats(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/courses/{courseId}/enrollments/approve")
    public ResponseEntity<Response> approveEnrollments(@PathVariable Long courseId) {
        var response=  adminService.approveByAvailableSlots(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}

