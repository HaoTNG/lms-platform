package com.example.lms.controller;

import com.example.lms.dto.*;
import com.example.lms.mapper.*;
import com.example.lms.model.*;
import com.example.lms.service.interf.MenteeService;
import com.example.lms.dto.Response;
import com.example.lms.service.interf.UserService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mentee")
@RequiredArgsConstructor
public class MenteeController {
    private final MenteeService menteeService;
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;
    private final LessonMapper lessonMapper;
    private final ResourceMapper resourceMapper;
    private final ExerciseMapper exerciseMapper;
    private final SubmissionMapper submissionMapper;
    private final SessionMapper sessionMapper;
    private final RatingMapper ratingMapper;
    private final QuestionMapper questionMapper;
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;
    private final UserService userService;
    // =============================
    // LẤY THÔNG TIN MENTEE
    // =============================
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getMentee(@PathVariable Long id) {
        Mentee mentee = menteeService.getMyProfile(id);
        return ResponseEntity.ok(userMapper.toUserDTO(mentee));
    }

    // =============================
    // ĐĂNG KÝ KHÓA HỌC
    // =============================
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollCourse(
            @RequestBody EnrollCourseRequest request) {
        Long id = userService.getCurrentUserId();
        menteeService.enrollCourse(id, request.getCourseId());
        return ResponseEntity.ok("You have successfully enrolled in the course.");
    }


    // =============================
    // LẤY DANH SÁCH KHÓA HỌC
    // =============================
    @GetMapping("/mycourses")
    public ResponseEntity<List<CourseDTO>> getMyCourses() {
        Long id = userService.getCurrentUserId();
        List<Course> courses = menteeService.getMyCourses(id);
        List<CourseDTO> dtoList = new ArrayList<>();
        for (Course course : courses) {
            dtoList.add(courseMapper.toDTO(course));
        }
        return ResponseEntity.ok(dtoList);
    }

    // =============================
    // XEM CHI TIẾT KHÓA HỌC
    // =============================
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseDTO> getCourseDetail(
            @PathVariable Long courseId
    ) {
        Long menteeId = userService.getCurrentUserId();
        Course course = menteeService.getMyCourseDetail(menteeId, courseId);
        CourseDTO dto = courseMapper.toDTO(course);
        return ResponseEntity.ok(dto);
    }

    // =============================
    // LẤY BÀI GIẢNG (LESSONS)
    // =============================
    @GetMapping("/course/{courseId}/lessons")
    public ResponseEntity<List<LessonDTO>> getLessons(@PathVariable Long courseId) {
        List<Lesson> lessons = menteeService.getLessonsByCourse(courseId);
        List<LessonDTO> dtoList = new ArrayList<>();
        for (Lesson lesson : lessons) {
            dtoList.add(lessonMapper.toLessonDTO(lesson));
        }
        return ResponseEntity.ok(dtoList);
    }

    // =============================
    // LẤY TÀI NGUYÊN TRONG BÀI GIẢNG
    // =============================
    @GetMapping("/lesson/{lessonId}/resources")
    public ResponseEntity<List<ResourceDTO>> getResources(@PathVariable Long lessonId) {
        List<Resource> resources = menteeService.getResourcesByLesson(lessonId);
        List<ResourceDTO> dtoList = new ArrayList<>();
        for (Resource resource : resources) {
            dtoList.add(resourceMapper.toResourceDTO(resource));
        }
        return ResponseEntity.ok(dtoList);
    }

    // =============================
    // LẤY EXERCISES TRONG BÀI GIẢNG
    // =============================
    @GetMapping("/lesson/{lessonId}/exercises")
    public ResponseEntity<List<ExerciseDTO>> getExercises(@PathVariable Long lessonId) {
        List<Exercise> exercises = menteeService.getExercisesByLesson(lessonId);
        List<ExerciseDTO> dtoList = new ArrayList<>();
        for (Exercise exercise : exercises) {
            dtoList.add(exerciseMapper.toExerciseDTO(exercise));
        }
        return ResponseEntity.ok(dtoList);
    }

    // =============================
    // NỘP BÀI
    // =============================
    @PostMapping("/exercise/{exerciseId}/submit")
    public ResponseEntity<SubmissionDTO> submitExercise(
            @PathVariable Long exerciseId,
            @RequestBody SubmissionDTO dto) {

        Long menteeId = userService.getCurrentUserId();
        dto.setMenteeId(menteeId);
        dto.setExerciseId(exerciseId);

        Submission entity = submissionMapper.toSubmission(dto);
        Submission saved = menteeService.submitExercise(menteeId, exerciseId, entity);

        return ResponseEntity.ok(submissionMapper.toSubmissionDTO(saved));
    }

    // =============================
    // XEM LỊCH HỌC (SESSIONS)
    // =============================
    @GetMapping("/course/{courseId}/sessions")
    public ResponseEntity<List<SessionDTO>> getSessions(@PathVariable Long courseId) {
        List<Session> sessions = menteeService.getSessionsByCourse(courseId);
        List<SessionDTO> dtoList = new ArrayList<>();
        for (Session session : sessions) {
            dtoList.add(sessionMapper.toSessionDTO(session));
        }
        return ResponseEntity.ok(dtoList);
    }

    // =============================
    // TẠO ĐÁNH GIÁ (RATING)
    // =============================
    @PostMapping("/session/{sessionId}/rating")
    public ResponseEntity<RatingDTO> rateSession(
            @PathVariable Long sessionId,
            @RequestBody CreateRatingRequest request) {

        Long menteeId = userService.getCurrentUserId();
        Rating saved = menteeService.rateSession(menteeId, sessionId,
                request.getScore(), request.getComment());

        RatingDTO dto = ratingMapper.toRatingDTO(saved);
        return ResponseEntity.ok(dto);
    }

    // =============================
    // LẤY RATING CỦA MENTEE
    // =============================
    @GetMapping("/ratings")
    public ResponseEntity<List<RatingDTO>> getMyRatings() {
        Long menteeId = userService.getCurrentUserId();
        List<Rating> ratings = menteeService.getMyRatings(menteeId);
        List<RatingDTO> dtoList = new ArrayList<>();
        for (Rating rating : ratings) {
            dtoList.add(ratingMapper.toRatingDTO(rating));
        }
        return ResponseEntity.ok(dtoList);
    }

    // =============================
    // LẤY QUESTION (FORUM)
    // =============================
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDTO>> getLessonQuestions() {
        Long menteeId = userService.getCurrentUserId();
        List<Question> questions = menteeService.getMyQuestions(menteeId);
        List<QuestionDTO> dtoList = new ArrayList<>();
        for (Question question : questions) {
            dtoList.add(questionMapper.toQuestionDTO(question));
        }
        return ResponseEntity.ok(dtoList);
    }

    // =============================
    // TẠO CÂU HỎI
    // =============================
    @PostMapping("/lesson/{forumId}/questions")
    public ResponseEntity<String> askQuestion(
            @PathVariable Long forumId,
            @RequestBody AskQuestionRequest request) {
        Long  menteeId = userService.getCurrentUserId();
        menteeService.askQuestion(menteeId, forumId, request.getContent());

        return ResponseEntity.ok("Your question has been submitted successfully.");
    }

    // =============================
    // LẤY CONVERSATION
    // =============================
    @GetMapping("/{menteeId}/conversations")
    public ResponseEntity<List<ConversationDTO>> getMyConversations(@PathVariable Long menteeId) {
        List<Conversation> conversations = menteeService.getMyConversations(menteeId);
        List<ConversationDTO> dtoList = new ArrayList<>();
        for (Conversation conversation : conversations) {
            dtoList.add(conversationMapper.toConversationDTO(conversation));
        }
        return ResponseEntity.ok(dtoList);
    }

    // =============================
    // LẤY MESSAGES TRONG MỘT CONVERSATION
    // =============================
    @GetMapping("/{menteeId}/conversation/{conversationId}/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long menteeId, @PathVariable Long conversationId) {

        List<Message> messages = menteeService.getMessages(conversationId);

        List<MessageDTO> dtoList = new ArrayList<>();
        for (Message message : messages) {
            dtoList.add(messageMapper.toMessageDTO(message));
        }

        return ResponseEntity.ok(dtoList);
    }


    // =============================
    // GỬI MESSAGE
    // =============================
    @PostMapping("/{menteeId}/conversation/{conversationId}/messages")
    public ResponseEntity<String> sendMessage(
            @PathVariable Long menteeId,
            @PathVariable Long conversationId,
            @RequestBody SendMessageRequest request) {

        menteeService.sendMessage(
                conversationId,
                menteeId,
                request.getReceiverId(),
                request.getContent()
        );

        return ResponseEntity.ok("Your message has been sent successfully.");
    }
}
