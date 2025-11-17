package com.example.lms.controller;



import com.example.lms.dto.Response;
import com.example.lms.dto.UserDTO;
import com.example.lms.dto.AnnouncementDTO;
import com.example.lms.model.Course;
import com.example.lms.service.interf.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // ==================== USER MANAGEMENT ====================
    @GetMapping("/manage-user")
    public ResponseEntity<Response> getUserList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Response response = adminService.ManageUser(page, size);
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
    public ResponseEntity<Response> createCourse(@RequestBody Course course) {
        Response response = adminService.createCourse(course);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ==================== REPORT TICKET MANAGEMENT ====================
    @GetMapping("/report-tickets")
    public ResponseEntity<Response> getAllReportTickets() {
        Response response = adminService.getAllReportTickets();
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
}

