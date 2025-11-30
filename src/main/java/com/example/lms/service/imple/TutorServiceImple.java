package com.example.lms.service.imple;

import com.example.lms.dto.ExerciseDTO;
import com.example.lms.dto.LessonDTO;
import com.example.lms.model.*;
import com.example.lms.repository.*;
import com.example.lms.service.interf.TutorService;
import com.example.lms.service.interf.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.lms.dto.Response;
import com.example.lms.mapper.CourseMapper;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorServiceImple implements TutorService {
    private final CourseRepository courseRepository;
    private final AdminService adminService;
    private final ExerciseRepository exerciseRepository;
    private final SubmissionRepository submissionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final RateRepository rateRepository;
    private final ConversationRepository conversationRepository;
    private final ReportTicketRepository reportTicketRepository;
    private final TutorRepository tutorRepository;
    private final MenteeRepository menteeRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;
    private final LessonRepository lessonRepository;

    @Override
    public Response updateCourse(Long courseId, Course courseDTO) {
        Response response = new Response();
        try {
            Course existingCourse = courseRepository.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

            if (courseDTO.getCourseName() != null) existingCourse.setCourseName(courseDTO.getCourseName());
            if (courseDTO.getDescription() != null) existingCourse.setDescription(courseDTO.getDescription());
            if (courseDTO.getMaxMentee() != null) existingCourse.setMaxMentee(courseDTO.getMaxMentee());
            if (courseDTO.getStartDate() != null) existingCourse.setStartDate(courseDTO.getStartDate());
            if (courseDTO.getEndDate() != null) existingCourse.setEndDate(courseDTO.getEndDate());
            if (courseDTO.getCourseStatus() != null) {
                existingCourse.setCourseStatus(courseDTO.getCourseStatus());
            }

            courseRepository.save(existingCourse);

            response.setStatusCode(200);
            response.setMessage("Course updated successfully");
        } catch (IllegalArgumentException e) {
            response.setStatusCode(400);
            response.setMessage("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to update course: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllRatings(Long sessionId, int page, int size) {
        try {
            var pageable = org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
            var pageResult = rateRepository.findBySessionId(sessionId, pageable);

            var pagination = com.example.lms.dto.Pagination.builder()
                    .content(pageResult.getContent())
                    .page(pageResult.getNumber())
                    .size(pageResult.getSize())
                    .totalItems(pageResult.getTotalElements())
                    .totalPages(pageResult.getTotalPages())
                    .hasNext(pageResult.hasNext())
                    .hasPrevious(pageResult.hasPrevious())
                    .build();

            return Response.builder()
                    .statusCode(200)
                    .message("Ratings retrieved successfully")
                    .pagination(pagination)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to retrieve ratings: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response replyToRating(Long ratingId, String replyComment) {
        try {
            var rating = rateRepository.findById(ratingId)
                    .orElseThrow(() -> new IllegalArgumentException("Rating not found with ID: " + ratingId));
            rating.setReplyComment(replyComment);
            rateRepository.save(rating);
            return Response.builder()
                    .statusCode(200)
                    .message("Reply saved successfully")
                    .data(rating)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.builder().statusCode(400).message(e.getMessage()).build();
        } catch (Exception e) {
            return Response.builder().statusCode(400).message("Failed to reply to rating: " + e.getMessage()).build();
        }
    }

    @Override
    public Response reportRating(Long ratingId, String title, String description) {
        try {
            var rating = rateRepository.findById(ratingId)
                    .orElseThrow(() -> new IllegalArgumentException("Rating not found with ID: " + ratingId));

            ReportTicket ticket = new ReportTicket();
            ticket.setMentee(rating.getMentee());
            ticket.setTitle(title);
            ticket.setDescription(description);

            var saved = reportTicketRepository.save(ticket);

            return Response.builder()
                    .statusCode(200)
                    .message("Rating reported successfully")
                    .reportTicket(saved)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.builder().statusCode(400).message(e.getMessage()).build();
        } catch (Exception e) {
            return Response.builder().statusCode(400).message("Failed to report rating: " + e.getMessage()).build();
        }
    }

    @Override
    public Response joinConversation(Long menteeId, Long tutorId) {
        try {
            var existing = conversationRepository.findByMenteeIdAndTutorId(menteeId, tutorId);
            if (existing.isPresent()) {
                return Response.builder().statusCode(200).message("Conversation retrieved").data(existing.get()).build();
            }

            var mentee = menteeRepository.findMenteeById(menteeId);
            var tutor = tutorRepository.findById(tutorId).orElseThrow(() -> new IllegalArgumentException("Tutor not found"));

            Conversation conv = Conversation.builder().mentee(mentee).tutor(tutor).build();
            var saved = conversationRepository.save(conv);
            return Response.builder().statusCode(200).message("Conversation created").data(saved).build();
        } catch (IllegalArgumentException e) {
            return Response.builder().statusCode(400).message(e.getMessage()).build();
        } catch (Exception e) {
            return Response.builder().statusCode(400).message("Failed to join conversation: " + e.getMessage()).build();
        }
    }

    @Override
    public Response sendMessage(Long conversationId, Long senderId, String content) {
        Response response = new Response();
        try {
            Conversation conversation = conversationRepository.findById(conversationId)
                    .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
            Tutor tutorUser = conversation.getTutor();
            Mentee menteeUser = conversation.getMentee();
            User receiver;

            if (sender.getId().equals(tutorUser.getId())) {
                receiver = menteeUser;
            } else if (sender.getId().equals(menteeUser.getId())) {
                receiver = tutorUser;
            } else {
                throw new IllegalArgumentException("Sender is not a participant in this conversation");
            }

            Message message = Message.builder()
                    .conversation(conversation)
                    .sender(sender)
                    .receiver(receiver)
                    .content(content)
                    .build();

            Message savedMessage = messageRepository.save(message);

            response.setStatusCode(200);
            response.setMessage("Message sent successfully");
            response.setData(savedMessage);

        } catch (IllegalArgumentException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Failed to send message: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteCourse(Long courseId) {
        Response response = new Response();
        try {
            Course existingCourse = courseRepository.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

            courseRepository.delete(existingCourse);

            response.setStatusCode(200);
            response.setMessage("Course deleted successfully");
        } catch (IllegalArgumentException e) {
            response.setStatusCode(400);
            response.setMessage("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to delete course: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCourseById(Long courseId) {
        Response response = new Response();
        try {
            Course existingCourse = courseRepository.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
            
            // Convert to DTO to avoid circular references
            var courseDTO = courseMapper.toDTO(existingCourse);
            
            response.setStatusCode(200);
            response.setMessage("Course retrieved successfully");
            response.setData(courseDTO);
        } catch (IllegalArgumentException e) {
            response.setStatusCode(400);
            response.setMessage("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Failed to retrieve course: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllMenteesInCourse(Long courseId, int page, int size) {
        try {
            var pageable = PageRequest.of(Math.max(0, page), Math.max(1, size));
            var course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            var pageResult = enrollmentRepository.findByCourse(course, pageable);

            var mentees = pageResult.map(Enrollment::getMentee);

            var pagination = com.example.lms.dto.Pagination.builder()
                    .content(mentees.getContent())
                    .page(mentees.getNumber())
                    .size(mentees.getSize())
                    .totalItems(mentees.getTotalElements())
                    .totalPages(mentees.getTotalPages())
                    .hasNext(mentees.hasNext())
                    .hasPrevious(mentees.hasPrevious())
                    .build();

            return Response.builder()
                    .statusCode(200)
                    .message("Mentees retrieved successfully")
                    .pagination(pagination)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to retrieve mentees: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response createExercise(ExerciseDTO exercise) {
        try {
            Exercise forsave = Exercise.builder()
                    .lesson(lessonRepository.getLessonById(exercise.getLessonId()))
                    .question(exercise.getQuestion())
                    .deadline(exercise.getDeadline())
                    .attemptLimit(exercise.getAttemptLimit())
                    .build();
            Exercise saved = exerciseRepository.save(forsave);
            return Response.builder()
                    .statusCode(200)
                    .message("Exercise created successfully")
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to create exercise: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response deleteExercise(Long exerciseId) {
        Response response = new Response();
        try {
            if (exerciseRepository.existsById(exerciseId)) {
                exerciseRepository.deleteById(exerciseId);
                response.setStatusCode(200);
                response.setMessage("Exercise deleted successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("Exercise not found");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting exercise: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllSubmissions(Long exerciseId, int page, int size) {
        try {
            var pageable = PageRequest.of(Math.max(0, page), Math.max(1, size));
            var pageResult = submissionRepository.findByExerciseId(exerciseId, pageable);

            var pagination = com.example.lms.dto.Pagination.builder()
                    .content(pageResult.getContent())
                    .page(pageResult.getNumber())
                    .size(pageResult.getSize())
                    .totalItems(pageResult.getTotalElements())
                    .totalPages(pageResult.getTotalPages())
                    .hasNext(pageResult.hasNext())
                    .hasPrevious(pageResult.hasPrevious())
                    .build();

            return Response.builder()
                    .statusCode(200)
                    .message("Submissions retrieved successfully")
                    .pagination(pagination)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to retrieve submissions: " + e.getMessage())
                    .build();
        }
    }


    @Override
    public Response gradeSubmission(Long submissionId, Double grade) {
        try {
            Submission submission = submissionRepository.findById(submissionId)
                    .orElseThrow(() -> new IllegalArgumentException("Submission not found with ID: " + submissionId));
            submission.setGrade(grade);
            submissionRepository.save(submission);
            return Response.builder()
                    .statusCode(200)
                    .message("Submission graded successfully")
                    .data(submission)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.builder()
                    .statusCode(400)
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to grade submission: " + e.getMessage())
                    .build();
        }
    }
    @Override
    public Response getAllLessonByCourseId(Long id) {
        try {
            // Lấy danh sách Lesson theo courseId
            List<Lesson> lessons = lessonRepository.findByCourse_CourseId(id);

            // Map sang DTO
            List<LessonDTO> dtos = lessons.stream()
                    .map(lesson -> {
                        LessonDTO dto = LessonDTO.builder()
                                .id(lesson.getId())
                                .courseId(lesson.getCourse().getCourseId())
                                .title(lesson.getTitle())
                                .description(lesson.getDescription())
                                .build();

                        return dto;
                    })
                    .toList();

            return Response.builder()
                    .statusCode(200)
                    .message("Lessons retrieved successfully")
                    .data(dtos)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to retrieve lessons: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getLessonByLessonIdAndCourseId(Long lessonId, Long courseId) {
        try{
            List<Lesson> lessons = lessonRepository.findByCourse_CourseId(courseId);

            // Map sang DTO
            Lesson lesson = lessons.stream()
                    .filter(l -> l.getId().equals(lessonId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));
            LessonDTO dto = LessonDTO.builder()
                    .id(lesson.getId())
                    .courseId(lesson.getCourse().getCourseId())
                    .title(lesson.getTitle())
                    .description(lesson.getDescription())
                    .exerciseIds(
                            lesson.getExercises() != null
                                    ? lesson.getExercises().stream().map(e -> e.getId()).toList()
                                    : Collections.emptyList()
                    )
                    .resourceIds(
                            lesson.getResources() != null
                                    ? lesson.getResources().stream().map(r -> r.getId()).toList()
                                    : Collections.emptyList()
                    )
                    .build();


            return Response.builder()
                    .statusCode(200)
                    .message("Lessons retrieved successfully")
                    .data(dto)
                    .build();
        }catch (Exception e){
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to retrieve lessons: " + e.getMessage())
                    .build();
        }
    }

    public Response createLesson(LessonDTO req) {
        try{
            if (req.getCourseId() == null) {
                throw new IllegalArgumentException("courseId is required for create");
            }

            Course course = courseRepository.findById(req.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with id=" + req.getCourseId()));

            Lesson lesson = Lesson.builder()
                    .course(course)
                    .title(req.getTitle())
                    .description(req.getDescription())
                    .build();

            Lesson saved = lessonRepository.save(lesson);
            return Response.builder()
                    .statusCode(200)
                    .message("successfully created lession")
                    .build();
        }catch (Exception e){
            return Response.builder().statusCode(400).message("Failed to create lesson: " + e.getMessage()).build();
        }

    }


    public Response updateLesson(Long id, LessonDTO req) {
        try{
            Lesson lesson = lessonRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Lesson not found with id=" + id));

            // chỉ update nếu field != null (patch-like)
            if (req.getTitle() != null) lesson.setTitle(req.getTitle());
            if (req.getDescription() != null) lesson.setDescription(req.getDescription());
            if (req.getCourseId() != null && !req.getCourseId().equals(lesson.getCourse().getCourseId())) {
                Course course = courseRepository.findById(req.getCourseId())
                        .orElseThrow(() -> new RuntimeException("Course not found with id=" + req.getCourseId()));
                lesson.setCourse(course);
            }

            Lesson saved = lessonRepository.save(lesson);
            return Response.builder()
                    .statusCode(200)
                    .message("successfully created lession")
                    .build();
        }catch (Exception e){
            return Response.builder().statusCode(400).message("Failed to create lesson: " + e.getMessage()).build();
        }
    }


    // Dùng lại method có sẵn trong adminService
    public Response getAllAnnouncements(Integer page, Integer size, String recipientType, String title, Long adminId) {
        return adminService.getAllAnnouncements(page, size, recipientType, title, adminId);
    }

    public Response getAnnouncementsByAdmin(Long adminId, int page, int size) {
        return adminService.getAnnouncementsByAdmin(adminId, page, size);
    }

    public Response deleteAnnouncement(Long announcementId) {
        return adminService.deleteAnnouncement(announcementId);
    }
}