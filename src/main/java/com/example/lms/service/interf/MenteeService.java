package com.example.lms.service.interf;

import com.example.lms.dto.*;
import com.example.lms.model.*;

import java.util.List;

public interface MenteeService {

    // ===== PROFILE =====
    Mentee getMyProfile(Long menteeId);

    // ===== COURSE / ENROLLMENT =====
    List<Course> getMyEnrollCourses(Long menteeId);
    List<Course> getMyCourses(Long menteeId);
    Course getMyCourseDetail(Long menteeId, Long courseId);

    Response enrollCourse(Long menteeId, Long courseId);
    Response unenrollCourse(Long menteeId, Long courseId);
    // ===== LESSON / RESOURCE / EXERCISE =====
    List<Lesson> getLessonsByCourse(Long courseId);

    List<Resource> getResourcesByLesson(Long lessonId);

    List<Exercise> getExercisesByLesson(Long lessonId);

    // ===== SUBMISSION =====
    Submission submitExercise(Long menteeId, Long exerciseId, Submission submission);

    List<Submission> getMySubmissions(Long menteeId);

    // ===== SESSION / ATTEND / JOIN CLASS =====
    List<Session> getSessionsByCourse(Long courseId);
    SessionDTO getSessionById(Long sessionId);
    List<Session> getMyUpcomingSessions(Long menteeId);

    // ===== RATING =====
    Rating rateSession(Long menteeId, Long sessionId, Integer score, String comment);

    List<Rating> getMyRatings(Long menteeId);

    // ===== QUESTION / FORUM =====
    Question askQuestion(Long menteeId, Long lessonId, String content);
    List<QuestionDTO> getQuestionsByForum(Long forumId);
    Forum createForum(Long sessionId);
    List<Question> getMyQuestions(Long menteeId);

    // ===== REPORT TICKET =====
    ReportTicket createReport(Long menteeId, String title, String content);

    Response getMyReports(Long menteeId);

    // ===== MESSAGING =====
    Conversation openConversation(Long menteeId, Long tutorId);

    List<Conversation> getMyConversations(Long menteeId);
    List<Message> getMessages(Long conversationId);
    Message sendMessage(Long conversationId, Long senderId, Long receiverId, String content);
}
