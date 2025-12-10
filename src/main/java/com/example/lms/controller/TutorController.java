package com.example.lms.controller;

import com.example.lms.dto.*;
import com.example.lms.enums.ResourceType;
import com.example.lms.mapper.ExerciseMapper;
import com.example.lms.mapper.ResourceMapper;
import com.example.lms.mapper.SessionMapper;
import com.example.lms.model.*;
import com.example.lms.repository.ExerciseRepository;
import com.example.lms.repository.LessonRepository;
import com.example.lms.repository.ResourceRepository;
import com.example.lms.service.interf.AdminService;
import com.example.lms.service.interf.MenteeService;
import com.example.lms.service.interf.TutorService;

import com.example.lms.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tutors")
@RequiredArgsConstructor
public class TutorController {
    private final TutorService tutorService;
    private final MenteeService menteeService;
    private final SessionMapper sessionMapper;
    private final UserService userService;
    private final ResourceMapper resourceMapper;
    private final LessonRepository lessonRepository;
    private final ResourceRepository resourceRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    // ==================== COURSE MANAGEMENT ====================

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Response> getCourseById(@PathVariable Long courseId){
        Response response = tutorService.getCourseById(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

   // @PutMapping("/courses/{courseId}")
   // public ResponseEntity<Response> updateCourse(@PathVariable Long courseId, @RequestBody Course course) {
    //    Response response = tutorService.updateCourse(courseId, course);
   //     return ResponseEntity.status(response.getStatusCode()).body(response);
   // }

   // @DeleteMapping("/courses/{courseId}")
  //  public ResponseEntity<Response> deleteCourse(@PathVariable Long courseId) {
   //     Response response = tutorService.deleteCourse(courseId);
   //     return ResponseEntity.status(response.getStatusCode()).body(response);
   // }

    // ==================== MENTEE & EXERCISE MANAGEMENT ====================

    @GetMapping("/courses/my")
    public ResponseEntity<Response> getMyCourse(){
        Response response = tutorService.getMyCourse();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

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
    public ResponseEntity<Response> createExercise(@RequestBody ExerciseDTO dto){ // dto này sẽ phải parse cái lesson id vào trong nhé
        Response response = tutorService.createExercise(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/exercises")
    public ResponseEntity<Response> getExercises(){
        Response response = tutorService.getExercise();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/exercises/{exerciseId}")
    public ResponseEntity<ExerciseDTO> getExercise(@PathVariable Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new RuntimeException("Exercise not found"));
        var exerciseDTO = exerciseMapper.toExerciseDTO(exercise);
        return ResponseEntity.ok(exerciseDTO);
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
    @GetMapping("/myexercises")
    public ResponseEntity<Response> getExercisesByTutor() {
        try {
            Long tutorId = userService.getCurrentUserId();

            // Raw data từ DB
            List<Exercise> rawExercises = exerciseRepository.findAllByTutorIdNative(tutorId);

            // Map sang DTO gọn
            List<ExerciseDTO> dtoList = rawExercises.stream()
                    .map(e -> ExerciseDTO.builder()
                            .id(e.getId())
                            .lessonId(e.getLesson().getId())
                            .question(e.getQuestion())
                            .deadline(e.getDeadline())
                            .attemptLimit(e.getAttemptLimit())
                            .submissionCount(
                                    e.getSubmissions() == null ? 0 : e.getSubmissions().size()
                            )
                            .build())
                    .toList();

            return ResponseEntity.ok(
                    Response.builder()
                            .statusCode(200)
                            .message("Successfully fetched exercises for tutor")
                            .data(dtoList)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .statusCode(400)
                            .message("Failed to fetch exercises: " + e.getMessage())
                            .build()
            );
        }
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

    @PostMapping("/submission")
    public ResponseEntity<Response> gradeSubmission(
            @RequestBody GradeRequest grade
    ) {
        Response response = tutorService.gradeSubmission(grade.getId(), grade.getGrade());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/myresources")
    public ResponseEntity<Response> getResourcesByTutor() {
        try {
            Long tutorId = userService.getCurrentUserId();

            List<Resource> rawResources = resourceRepository.findAllByTutorIdNative(tutorId);

            List<ResourceDTO> dtoList = rawResources.stream()
                    .map(r -> ResourceDTO.builder()
                            .id(r.getId())
                            .lessonId(r.getLesson().getId())
                            .title(r.getTitle())
                            .fileUrl(r.getFileUrl())
                            .resourceType(r.getResourceType().name())
                            .build()
                    )
                    .toList();

            return ResponseEntity.ok(
                    Response.builder()
                            .statusCode(200)
                            .message("Successfully fetched resources for tutor")
                            .data(dtoList)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .statusCode(400)
                            .message("Failed to fetch resources: " + e.getMessage())
                            .build()
            );
        }
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
    // ==================== SUBJECT REGISTRATION MANAGEMENT ====================

    @GetMapping("/subject-registrations")
    public ResponseEntity<Response> getAllSubjectRegistrationsByTutorId() {
        Response response = tutorService.getAllSubjectRegistrationsByTutorId();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/subject-registrations")
    public ResponseEntity<Response> subjectRegistration(
            @RequestBody SubjectRegistrationRequest subjectId
    ) {
        Response response = tutorService.subjectRegistration(subjectId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/subjects")
    public ResponseEntity<Response> getSubjects(){
        Response response = tutorService.getSubjects();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/course/{courseId}/sessions")
    public ResponseEntity<List<SessionDTO>> getSessions(@PathVariable Long courseId) {
        List<Session> sessions = menteeService.getSessionsByCourse(courseId);
        List<SessionDTO> dtoList = new ArrayList<>();
        for (Session session : sessions) {
            dtoList.add(sessionMapper.toSessionDTO(session));
        }
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/course/{courseId}/sessions")
    public ResponseEntity<Response> createSession(
            @PathVariable Long courseId,
            @RequestBody SessionDTO session) {
        Response response = tutorService.createSession(courseId, session);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/course/{courseId}/sessions")
    public ResponseEntity<Response> updateSession(
            @PathVariable Long courseId,
            @RequestBody SessionDTO session) {
        Response response = tutorService.updateSession(courseId, session);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/course/sessions/{sessionId}")
    public ResponseEntity<SessionDTO> getSession(@PathVariable Long sessionId) {
        SessionDTO session = menteeService.getSessionById(sessionId);
        return ResponseEntity.status(200).body(session);
    }
    // ==================== LESSON MANAGEMENT ====================
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

    @GetMapping("/lesson/{lessonId}/resources")
    public ResponseEntity<List<ResourceDTO>> getResources(@PathVariable Long lessonId) {
        List<Resource> resources = menteeService.getResourcesByLesson(lessonId);
        List<ResourceDTO> dtoList = new ArrayList<>();
        for (Resource resource : resources) {
            dtoList.add(resourceMapper.toResourceDTO(resource));
        }
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/resources")
    public ResponseEntity<Response> createResource(@RequestBody ResourceDTO req) {
        try {
            Lesson lesson = lessonRepository.findById(req.getLessonId()).orElse(null);
            if (lesson == null) {
                return ResponseEntity.badRequest()
                        .body(Response.builder().statusCode(400).message("Lesson not found").build());
            }

            Resource resource = Resource.builder()
                    .lesson(lesson)
                    .title(req.getTitle())
                    .fileUrl(req.getFileUrl())
                    .resourceType(ResourceType.fromValue(req.getResourceType()))
                    .build();

            resourceRepository.save(resource);

            return ResponseEntity.ok(
                    Response.builder().statusCode(200).message("Successfully created resource").data(resource).build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.builder().statusCode(400)
                            .message("Failed to create resource: " + e.getMessage()).build());
        }
    }

    @PutMapping("/resources/{id}")
    public ResponseEntity<Response> updateResource(@PathVariable Long id, @RequestBody ResourceDTO req) {
        try {
            Resource resource = resourceRepository.findById(id).orElse(null);
            if (resource == null) {
                return ResponseEntity.badRequest()
                        .body(Response.builder().statusCode(400).message("Resource not found").build());
            }

            // Update lesson nếu FE gửi lessonId mới
            if (req.getLessonId() != null) {
                Lesson lesson = lessonRepository.findById(req.getLessonId()).orElse(null);
                if (lesson == null) {
                    return ResponseEntity.badRequest()
                            .body(Response.builder().statusCode(400).message("Lesson not found").build());
                }
                resource.setLesson(lesson);
            }

            if (req.getTitle() != null) {
                resource.setTitle(req.getTitle());
            }

            if (req.getFileUrl() != null) {
                resource.setFileUrl(req.getFileUrl());
            }

            if (req.getResourceType() != null) {
                resource.setResourceType(ResourceType.fromValue(req.getResourceType()));
            }

            resourceRepository.save(resource);

            return ResponseEntity.ok(
                    Response.builder()
                            .statusCode(200)
                            .message("Successfully updated resource")
                            .data(resource)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.builder()
                            .statusCode(400)
                            .message("Failed to update resource: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/resources/{id}")
    public ResponseEntity<Response> deleteResource(@PathVariable Long id) {
        try {
            Resource resource = resourceRepository.findById(id).orElse(null);

            if (resource == null) {
                return ResponseEntity.status(404).body(
                        Response.builder()
                                .statusCode(404)
                                .message("Resource not found")
                                .build()
                );
            }

            resourceRepository.delete(resource);

            return ResponseEntity.ok(
                    Response.builder()
                            .statusCode(200)
                            .message("Successfully deleted resource")
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .statusCode(400)
                            .message("Failed to delete resource: " + e.getMessage())
                            .build()
            );
        }
    }


    @PostMapping("/forum/{sessionId}")
    public ResponseEntity<?> createForum(
            @PathVariable Long sessionId) {

        Forum forum = menteeService.createForum(sessionId);

        return ResponseEntity.ok(200);

    }

    @GetMapping("/forum/{forumId}/questions")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByForum(@PathVariable Long forumId) {
        return ResponseEntity.ok(menteeService.getQuestionsByForum(forumId));
    }

    @PostMapping("forum/{questionId}/answer")
    public ResponseEntity<Response> answerQuestion(
            @PathVariable Long questionId,
            @RequestBody String answer
    ){
       Response response = tutorService.answerQuestion(questionId,answer);
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


}