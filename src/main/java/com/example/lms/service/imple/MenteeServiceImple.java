package com.example.lms.service.imple;

import com.example.lms.dto.QuestionDTO;
import com.example.lms.dto.ReportTicketDTO;
import com.example.lms.dto.Response;
import com.example.lms.dto.SessionDTO;
import com.example.lms.mapper.QuestionMapper;
import com.example.lms.mapper.ReportTicketMapper;
import com.example.lms.mapper.SessionMapper;
import com.example.lms.model.*;
import com.example.lms.repository.*;
import com.example.lms.service.interf.MenteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.lms.enums.ReportTicketStatus;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MenteeServiceImple implements MenteeService {

    private final UserRepository userRepository;
    private final MenteeRepository menteeRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final ResourceRepository resourceRepository;
    private final ExerciseRepository exerciseRepository;
    private final SubmissionRepository submissionRepository;
    private final RateRepository rateRepository;
    private final SessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final ReportTicketRepository reportTicketRepository;
    private final ConversationRepository conversationRepository;
    private final ForumRepository forumRepository;
    private final MessageRepository messageRepository;
    private final SessionMapper sessionMapper;
    private final QuestionMapper questionMapper;
    private final ReportTicketMapper reportTicketMapper;

    // ============================= PROFILE =============================
    @Override
    public Mentee getMyProfile(Long menteeId) {
        return menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));
    }

    // ============================= COURSE =============================
    @Override
    public List<Course> getMyEnrollCourses(Long menteeId) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));
        List<Enrollment> Gotenrollments = mentee.getEnrollments();
        //List<Enrollment> enrollments = Gotenrollments.stream().filter(e -> e.getStatus().name().equals("ACTIVE")).toList();
        List<Enrollment> enrollments = Gotenrollments.stream().toList();
        List<Course> courses = new ArrayList<>(enrollments.size());
        for (Enrollment enrollment : enrollments) {
            courses.add(enrollment.getCourse());
        }
        return courses;
    }

    @Override
    public List<Course> getMyCourses(Long menteeId) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));
        List<Enrollment> Gotenrollments = mentee.getEnrollments();
        List<Enrollment> enrollments = Gotenrollments.stream().filter(e -> e.getStatus().name().equals("ACTIVE")).toList();
        //List<Enrollment> enrollments = Gotenrollments.stream().toList();
        List<Course> courses = new ArrayList<>(enrollments.size());
        for (Enrollment enrollment : enrollments) {
            courses.add(enrollment.getCourse());
        }
        return courses;
    }

    @Override
    public Course getMyCourseDetail(Long menteeId, Long courseId) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));
        for (Enrollment e : mentee.getEnrollments()) {
            if (e.getCourse().getCourseId().equals(courseId)) {
                return e.getCourse();
            }
        }
        throw new RuntimeException("Mentee is not enrolled in this course");
    }


    @Override
    public Response enrollCourse(Long menteeId, Long courseId) {
        try{
            Mentee mentee = menteeRepository.findById(menteeId)
                    .orElseThrow(() -> new RuntimeException("Mentee not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            Enrollment enrollment = Enrollment.builder()
                    .mentee(mentee)
                    .course(course)
                    .build();

            enrollmentRepository.save(enrollment);
            return Response.builder().statusCode(200).message("Success").build();
        } catch (Exception e) {
            return Response.builder().statusCode(500).message("Failed").build();
        }
    }
    @Override
    public Response unenrollCourse(Long menteeId, Long courseId) {
        Response response = new Response();

        try {
            // 1. Tìm mentee
            Mentee mentee = menteeRepository.findById(menteeId)
                    .orElseThrow(() -> new RuntimeException("Mentee not found"));

            // 2. Tìm course
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // 3. Tìm enrollment theo mentee + course
            Enrollment enrollment = enrollmentRepository
                    .findByMentee_IdAndCourse_CourseId(menteeId, courseId)
                    .orElseThrow(() -> new RuntimeException("Enrollment not found for this mentee and course"));

            // 4. Xoá
            enrollmentRepository.delete(enrollment);

            return Response.builder().statusCode(200).message("Success").build();
        } catch (Exception e) {
            return Response.builder().statusCode(500).message("Failed").build();
        }
    }

    // ============================= LESSON / RESOURCES =============================
    @Override
    public List<Lesson> getLessonsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getLessons();
    }

    @Override
    public List<Resource> getResourcesByLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        return lesson.getResources();
    }

    @Override
    public List<Exercise> getExercisesByLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        return lesson.getExercises();
    }

    // ============================= SUBMISSION =============================
    @Override
    public Submission submitExercise(Long menteeId, Long exerciseId, Submission submission) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));

        Exercise ex = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        submission.setMentee(mentee);
        submission.setExercise(ex);

        return submissionRepository.save(submission);
    }

    @Override
    public List<Submission> getMySubmissions(Long menteeId) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        return mentee.getSubmissions();
    }

    // ============================= SESSIONS =============================
    @Override
    public List<Session> getSessionsByCourse(Long courseId) {
        Course course = courseRepository.findById((courseId))
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getSessions();
    }
    @Override
    public SessionDTO getSessionById(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        return sessionMapper.toSessionDTO(session);
    }

    @Override
    public List<Session> getMyUpcomingSessions(Long menteeId) {
        return sessionRepository.findUpcomingSessionsForMentee(menteeId);
    }

    // ============================= RATING =============================
    @Override
    public Rating rateSession(Long menteeId, Long sessionId, Integer score, String comment) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Rating rating = Rating.builder()
                .mentee(mentee)
                .session(session)
                .score(score)
                .comment(comment)
                .build();

        return rateRepository.save(rating);
    }

    @Override
    public List<Rating> getMyRatings(Long menteeId) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));
        return mentee.getMyRatings();
    }

    // ============================= QUESTIONS / FORUM =============================
    @Override
    public Question askQuestion(Long menteeId, Long forumId, String content) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));

        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new RuntimeException("forum not found"));

        Question question = Question.builder()
                .mentee(mentee)
                .forum(forum)
                .content(content)
                .build();

        return questionRepository.save(question);
    }
    @Override
    public List<QuestionDTO> getQuestionsByForum(Long forumId) {

        // Kiểm tra forum tồn tại
        forumRepository.findById(forumId)
                .orElseThrow(() -> new RuntimeException("Forum not found"));

        // Query toàn bộ câu hỏi theo forumId
        return questionRepository.findByForumId(forumId)
                .stream()
                .map(questionMapper::toQuestionDTO)
                .toList();
    }
    @Override
    public Forum createForum(Long sessionId) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Nếu đã có forum thì trả về luôn
        if (session.getForum() != null) {
            return session.getForum();
        }

        Forum forum = new Forum();
        forum.setSession(session); // ID của forum tự = sessionId

        Forum saved = forumRepository.save(forum);

        return saved;
    }


    @Override
    public List<Question> getMyQuestions(Long menteeId) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));
        return mentee.getMyQuestions();
    }

    // ============================= REPORT TICKET =============================
    @Override
    public ReportTicket createReport(Long menteeId, String title, String description) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));

        ReportTicket ticket = ReportTicket.builder()
                .mentee(mentee)
                .title(title)
                .description(description)
                .status(ReportTicketStatus.PENDING)
                .build();

        return reportTicketRepository.save(ticket);
    }

    @Override
    public Response getMyReports(Long menteeId) {
        try{
            List<ReportTicket> rp = reportTicketRepository.findByMenteeId(menteeId);
            List<ReportTicketDTO> rpDTO = rp.stream().map(reportTicketMapper::toDTO).toList();

            return Response.builder().statusCode(200).message("success").data(rpDTO).build();
        }catch (Exception e){
            return Response.builder().statusCode(500).message("failed").build();
        }
    }

    // ============================= MESSAGING =============================
    @Override
    public Conversation openConversation(Long menteeId, Long tutorId) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentee not found"));

        var temp = conversationRepository.findByMenteeIdAndTutorId(menteeId, tutorId)
                .orElse(null);
        if (temp != null) return temp;

        Tutor tutor = new Tutor();
        tutor.setId(tutorId);

        Conversation newConversation = Conversation.builder()
                .mentee(mentee)
                .tutor(tutor)
                .build();

        return conversationRepository.save(newConversation);
    }

    @Override
    public List<Conversation> getMyConversations(Long menteeId) {
        Mentee mentee = menteeRepository.findById(menteeId)
                .orElseThrow(()->new RuntimeException("Mentee not found"));
        return mentee.getConversations();
    }
    @Override
    public List<Message> getMessages(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        return conversation.getMessages();
    }

    @Override
    public Message sendMessage(Long conversationId, Long senderId, Long receiverId, String content) {

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .isRead(false)
                .build();

        Message saved = messageRepository.save(message);

        conversation.setLastMessageAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        return saved;
    }

}