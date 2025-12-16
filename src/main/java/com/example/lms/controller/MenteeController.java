package com.example.lms.controller;

import com.example.lms.dto.*;
import com.example.lms.mapper.*;
import com.example.lms.model.*;
import com.example.lms.repository.ExerciseRepository;
import com.example.lms.repository.LessonRepository;
import com.example.lms.repository.SubmissionRepository;
import com.example.lms.service.interf.AdminService;
import com.example.lms.service.interf.MenteeService;
import com.example.lms.dto.Response;
import com.example.lms.service.interf.UserService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
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
    private final AdminService adminService;
    private final LessonRepository lessonRepository;
    private final ExerciseRepository exerciseRepository;
    private final SubmissionRepository submissionRepository;
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
        Response response = menteeService.enrollCourse(id, request.getCourseId());
        return ResponseEntity.status(200).body(response.getStatusCode());
    }

    @PostMapping("/unenroll")
    public ResponseEntity<?> unenrollCourse(
            @RequestBody EnrollCourseRequest request) {
        Long id = userService.getCurrentUserId();
        Response response = menteeService.unenrollCourse(id, request.getCourseId());
        return ResponseEntity.status(200).body(response.getStatusCode());
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

    @GetMapping("/myenrollcourses")
    public ResponseEntity<List<CourseDTO>> getMyEnrollCourses() {
        Long id = userService.getCurrentUserId();
        List<Course> courses = menteeService.getMyEnrollCourses(id);
        List<CourseDTO> dtoList = new ArrayList<>();
        for (Course course : courses) {
            dtoList.add(courseMapper.toDTO(course));
        }
        return ResponseEntity.ok(dtoList);
    }

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

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<LessonDTO> getLessonDetail(@PathVariable Long lessonId) {
        var lesson = lessonRepository.getLessonById(lessonId);
        var lessonDTO = lessonMapper.toLessonDTO(lesson);
        return ResponseEntity.ok(lessonDTO);
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

        // kiểm tra đã nộp chưa
        Optional<Submission> existingOpt = submissionRepository.findByMenteeIdAndExerciseId(menteeId, exerciseId);
        Submission saved;
        if (existingOpt.isPresent()) {
            Submission existing = existingOpt.get();
            existing.setTextAnswer(entity.getTextAnswer()); // hoặc set các field khác
            existing.setSubmittedAt(LocalDateTime.now());
            saved = submissionRepository.save(existing);
        } else {
            saved = submissionRepository.save(entity);
        }

        return ResponseEntity.ok(submissionMapper.toSubmissionDTO(saved));
    }

    @GetMapping("/exercise/{exerciseId}")
    public ResponseEntity<ExerciseDTO> getExercise(@PathVariable Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new RuntimeException("Exercise not found"));
        var exerciseDTO = exerciseMapper.toExerciseDTO(exercise);
        return ResponseEntity.ok(exerciseDTO);
    }

    @GetMapping("/exercise/{exerciseId}/submissions")
    public ResponseEntity<List<SubmissionDTO>> getSubmissions(@PathVariable Long exerciseId) {
        Long menteeId = userService.getCurrentUserId(); // Lấy user hiện tại

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        // Lọc chỉ submission của menteeId hiện tại
        List<SubmissionDTO> dtos = exercise.getSubmissions().stream()
                .filter(submission -> submission.getMentee().getId().equals(menteeId))
                .map(submissionMapper::toSubmissionDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }


    @PostMapping("/report-tickets")
    public ResponseEntity<Response> createReportTicket(@RequestBody ReportTicketDTO reportTicket) {
        Response response = adminService.createReportTicket(reportTicket);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/report-tickets")
    public ResponseEntity<Response> getAllReportTickets(

    ) {
        Long menteeId = userService.getCurrentUserId();
        Response response = menteeService.getMyReports(menteeId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
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

    @GetMapping("/course/sessions/{sessionId}")
    public ResponseEntity<SessionDTO> getSession(@PathVariable Long sessionId) {
        SessionDTO session = menteeService.getSessionById(sessionId);
        return ResponseEntity.status(200).body(session);
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

    @GetMapping("/forum/{forumId}/questions")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByForum(@PathVariable Long forumId) {
        return ResponseEntity.ok(menteeService.getQuestionsByForum(forumId));
    }


    // =============================
    // TẠO CÂU HỎI
    // =============================
    @PostMapping("/forum/{forumId}/questions")
    public ResponseEntity<String> askQuestion(
            @PathVariable Long forumId,
            @RequestBody AskQuestionRequest request) {
        Long  menteeId = userService.getCurrentUserId();
        menteeService.askQuestion(menteeId, forumId, request.getContent());

        return ResponseEntity.ok("Your question has been submitted successfully.");
    }

    @PostMapping("/forum/{sessionId}")
    public ResponseEntity<?> createForum(
            @PathVariable Long sessionId) {

        Forum forum = menteeService.createForum(sessionId);

        return ResponseEntity.ok(200);

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
