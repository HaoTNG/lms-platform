package com.example.lms.service.interf;

import com.example.lms.dto.Response;
import com.example.lms.model.Course;
import com.example.lms.model.Exercise;

public interface TutorService {
    // Course management
    Response updateCourse(Long courseId, Course course);
    Response deleteCourse(Long courseId);
    Response getCourseById(Long courseId);

    // Mentee management
    Response getAllMenteesInCourse(Long courseId, int page, int size);

    // Exercise management
    Response createExercise(Exercise exercise);
    Response deleteExercise(Long exerciseId);

    // Submission management
    Response getAllSubmissions(Long exerciseId, int page, int size);
    Response gradeSubmission(Long submissionId, Double grade);

    // Rating management
    Response getAllRatings(Long sessionId, int page, int size);
    Response replyToRating(Long ratingId, String replyComment);
    Response reportRating(Long ratingId, String title, String description);

    // Conversation management
    Response joinConversation(Long menteeId, Long tutorId);
    Response sendMessage(Long conversationId, Long senderId, String content);

    // Announcement management
    Response getAllAnnouncements(Integer page, Integer size, String recipientType, String title, Long adminId);
    Response getAnnouncementsByAdmin(Long adminId, int page, int size);
    Response deleteAnnouncement(Long announcementId);

    //
}
