package com.example.lms.controller;

import com.example.lms.dto.ExerciseDTO;
import com.example.lms.model.Course;
import com.example.lms.service.interf.TutorService;
import com.example.lms.dto.LessonDTO;
import com.example.lms.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tutors")
@RequiredArgsConstructor
public class TutorController {
    private final TutorService tutorService;
    // ==================== COURSE MANAGEMENT ====================

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Response> getCourseById(@PathVariable Long courseId){
        Response response = tutorService.getCourseById(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/courses/{courseId}")
    public ResponseEntity<Response> updateCourse(@PathVariable Long courseId, @RequestBody Course course) {
        Response response = tutorService.updateCourse(courseId, course);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Response> deleteCourse(@PathVariable Long courseId) {
        Response response = tutorService.deleteCourse(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ==================== MENTEE & EXERCISE MANAGEMENT ====================

    @GetMapping("/courses/{courseId}/mentees")
    public ResponseEntity<Response> getAllMenteesInCourse(
            @PathVariable Long courseId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Response response = tutorService.getAllMenteesInCourse(courseId, page, size);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/exercises")
    public ResponseEntity<Response> createExercise(@RequestBody ExerciseDTO dto){
        Response response = tutorService.createExercise(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/exercises/{exerciseId}")
    public ResponseEntity<Response> deleteExercise(@PathVariable Long exerciseId) {
        Response response = tutorService.deleteExercise(exerciseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ==================== SUBMISSION MANAGEMENT ====================

    @GetMapping("/exercises/{exerciseId}/submissions")
    public ResponseEntity<Response> getAllSubmissions(
            @PathVariable Long exerciseId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Response response = tutorService.getAllSubmissions(exerciseId, page, size);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/submissions/{submissionId}/grade")
    public ResponseEntity<Response> gradeSubmission(@PathVariable Long submissionId,
                                                    @RequestParam Double grade) {
        Response response = tutorService.gradeSubmission(submissionId, grade);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ==================== RATING MANAGEMENT ====================

    @GetMapping("/sessions/{sessionId}/ratings")
    public ResponseEntity<Response> getAllRatings(
            @PathVariable Long sessionId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Response response = tutorService.getAllRatings(sessionId, page, size);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/ratings/{ratingId}/reply")
    public ResponseEntity<Response> replyToRating(@PathVariable Long ratingId,
                                                  @RequestParam String reply) {
        Response response = tutorService.replyToRating(ratingId, reply);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/ratings/{ratingId}/report")
    public ResponseEntity<Response> reportRating(@PathVariable Long ratingId,
                                                 @RequestBody Map<String, String> body) {
        String title = body.getOrDefault("title", "Report Rating");
        String description = body.getOrDefault("description", "");
        Response response = tutorService.reportRating(ratingId, title, description);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ==================== CONVERSATION & MESSAGE ====================

    @PostMapping("/conversations/join")
    public ResponseEntity<Response> joinConversation(@RequestParam Long menteeId,
                                                     @RequestParam Long tutorId) {
        Response response = tutorService.joinConversation(menteeId, tutorId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<Response> sendMessage(
            @PathVariable Long conversationId,
            @RequestBody Map<String, Object> payload
    ) {
        Long senderId = Long.valueOf(payload.get("senderId").toString());
        String content = (String) payload.get("content");

        Response response = tutorService.sendMessage(conversationId, senderId, content);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/lessons/courses/{id}")
    public ResponseEntity<Response> getLessonById(@PathVariable Long id) {
        Response response = tutorService.getAllLessonByCourseId(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/lessons/courses/{courseId}/lessons/{lessonId}")
    public ResponseEntity<Response> getLessonByIdAndCourseId(
            @PathVariable Long courseId,
            @PathVariable Long lessonId
    ) {
        Response res = tutorService.getLessonByLessonIdAndCourseId(lessonId, courseId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
    @PostMapping("/lessons")
    public ResponseEntity<Response> create(@RequestBody LessonDTO req) {
        Response response = tutorService.createLesson(req);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/lessons/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody LessonDTO req) {
        Response response = tutorService.updateLesson(id, req);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    // ==================== ANNOUNCEMENT MANAGEMENT ====================

    @GetMapping("/announcements")
    public ResponseEntity<Response> getAllAnnouncements(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(required = false) String recipientType,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long adminId
    ) {
        Response response = tutorService.getAllAnnouncements(page, size, recipientType, title, adminId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/announcements/admin/{adminId}")
    public ResponseEntity<Response> getAnnouncementsByAdmin(
            @PathVariable Long adminId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Response response = tutorService.getAnnouncementsByAdmin(adminId, page, size);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/announcements/{announcementId}")
    public ResponseEntity<Response> deleteAnnouncement(@PathVariable Long announcementId) {
        Response response = tutorService.deleteAnnouncement(announcementId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}