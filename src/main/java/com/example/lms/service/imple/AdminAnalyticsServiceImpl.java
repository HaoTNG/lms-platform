package com.example.lms.service.imple;

import com.example.lms.dto.AdminAnalyticsDTO;
import com.example.lms.enums.CourseStatus;
import com.example.lms.model.*;
import com.example.lms.repository.*;
import com.example.lms.service.interf.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {
    
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SubmissionRepository submissionRepository;
    private final TutorRepository tutorRepository;
    private final MenteeRepository menteeRepository;
    private final SessionRepository sessionRepository;
    
    @Override
    public AdminAnalyticsDTO getAllAnalytics() {
        return AdminAnalyticsDTO.builder()
                .systemStats(getSystemStatistics())
                .studentAnalytics(getStudentAnalytics())
                .tutorAnalytics(getTutorAnalytics())
                .build();
    }
    
    @Override
    public AdminAnalyticsDTO.SystemStatsDTO getSystemStatistics() {
        List<User> allUsers = userRepository.findAll();
        
        long totalAdmins = allUsers.stream()
                .filter(u -> u instanceof Admin)
                .count();
        
        long totalTutors = allUsers.stream()
                .filter(u -> u instanceof Tutor)
                .count();
        
        long totalMentees = allUsers.stream()
                .filter(u -> u instanceof Mentee)
                .count();
        
        List<Course> allCourses = courseRepository.findAll();
        long activeCourses = allCourses.stream()
                .filter(c -> c.getCourseStatus() == CourseStatus.OPEN)
                .count();
        
        long upcomingCourses = 0;
        long finishedCourses = 0;
        
        Long totalEnrollments = courseRepository.findAll().stream()
                .mapToLong(c -> c.getEnrollments() != null ? c.getEnrollments().size() : 0)
                .sum();
        
        return AdminAnalyticsDTO.SystemStatsDTO.builder()
                .totalUsers((long) allUsers.size())
                .totalAdmins(totalAdmins)
                .totalTutors(totalTutors)
                .totalMentees(totalMentees)
                .totalCourses((long) allCourses.size())
                .activeCourses(activeCourses)
                .upcomingCourses(upcomingCourses)
                .finishedCourses(finishedCourses)
                .totalEnrollments(totalEnrollments)
                .build();
    }
    
    @Override
    public AdminAnalyticsDTO.StudentAnalyticsDTO getStudentAnalytics() {
        List<Mentee> mentees = menteeRepository.findAll();
        
        long excellentCount = 0;
        long goodCount = 0;
        long averageCount = 0;
        long weakCount = 0;
        double totalScore = 0;
        
        for (Mentee mentee : mentees) {
            List<Submission> submissions = submissionRepository.findAll().stream()
                    .filter(s -> s.getMentee() != null && s.getMentee().getId().equals(mentee.getId()))
                    .collect(Collectors.toList());
            
            if (!submissions.isEmpty()) {
                double avgScore = submissions.stream()
                        .mapToDouble(s -> s.getGrade() != null ? s.getGrade() : 0)
                        .average()
                        .orElse(0);
                
                totalScore += avgScore;
                
                if (avgScore >= 8.5) {
                    excellentCount++;
                } else if (avgScore >= 7) {
                    goodCount++;
                } else if (avgScore >= 5.5) {
                    averageCount++;
                } else {
                    weakCount++;
                }
            }
        }
        
        double averageScoreOverall = mentees.isEmpty() ? 0 : totalScore / mentees.size();
        
        // Submission analysis
        List<Submission> allSubmissions = submissionRepository.findAll();
        long onTimeSubmissions = allSubmissions.stream()
                .filter(s -> s.getSubmittedAt() != null && 
                        s.getExercise() != null &&
                        s.getExercise().getDeadline() != null &&
                        s.getSubmittedAt().isBefore(s.getExercise().getDeadline()))
                .count();
        
        long lateSubmissions = allSubmissions.stream()
                .filter(s -> s.getSubmittedAt() != null && 
                        s.getExercise() != null &&
                        s.getExercise().getDeadline() != null &&
                        s.getSubmittedAt().isAfter(s.getExercise().getDeadline()))
                .count();
        
        double completionRate = allSubmissions.isEmpty() ? 0 : 
                (onTimeSubmissions + lateSubmissions) * 100.0 / allSubmissions.size();
        
        return AdminAnalyticsDTO.StudentAnalyticsDTO.builder()
                .performanceDistribution(AdminAnalyticsDTO.PerformanceDistributionDTO.builder()
                        .excellentCount(excellentCount)
                        .goodCount(goodCount)
                        .averageCount(averageCount)
                        .weakCount(weakCount)
                        .averageScoreOverall(averageScoreOverall)
                        .build())
                .submissionAnalysis(AdminAnalyticsDTO.SubmissionAnalysisDTO.builder()
                        .onTimeSubmissions(onTimeSubmissions)
                        .lateSubmissions(lateSubmissions)
                        .completionRate(completionRate)
                        .build())
                .build();
    }
    
    @Override
    public AdminAnalyticsDTO.TutorAnalyticsDTO getTutorAnalytics() {
        List<Tutor> tutors = tutorRepository.findAll();
        Map<String, AdminAnalyticsDTO.TutorPerformanceDTO> tutorPerformances = new HashMap<>();
        
        for (Tutor tutor : tutors) {
            // Get courses for this tutor through SubjectRegistration
            List<Course> tutorCourses = courseRepository.findAll().stream()
                    .filter(c -> c.getSubjectRegistration() != null && 
                            c.getSubjectRegistration().getTutor() != null &&
                            c.getSubjectRegistration().getTutor().getId().equals(tutor.getId()))
                    .collect(Collectors.toList());
            
            // Calculate average rating from sessions taught by this tutor
            double averageRating = 0;
            if (!tutorCourses.isEmpty()) {
                List<Session> tutorSessions = sessionRepository.findAll().stream()
                        .filter(s -> s.getCourse() != null && tutorCourses.contains(s.getCourse()))
                        .collect(Collectors.toList());
                
                if (!tutorSessions.isEmpty()) {
                    averageRating = tutorSessions.stream()
                            .mapToDouble(Session::getAverageRating)
                            .average()
                            .orElse(0);
                }
            }
            
            long completedCourses = tutorCourses.stream()
                    .filter(c -> c.getCourseStatus() == CourseStatus.END)
                    .count();
            
            double courseCompletionRate = tutorCourses.isEmpty() ? 0 :
                    completedCourses * 100.0 / tutorCourses.size();
            
            // Get student distribution for this tutor's courses
            AdminAnalyticsDTO.StudentDistributionDTO studentDist = 
                    calculateStudentDistributionForTutor(tutorCourses);
            
            tutorPerformances.put(tutor.getName(), AdminAnalyticsDTO.TutorPerformanceDTO.builder()
                    .tutorId(tutor.getId())
                    .tutorName(tutor.getName())
                    .averageRating(averageRating)
                    .totalCourses((long) tutorCourses.size())
                    .completedCourses(completedCourses)
                    .courseCompletionRate(courseCompletionRate)
                    .studentDistribution(studentDist)
                    .build());
        }
        
        return AdminAnalyticsDTO.TutorAnalyticsDTO.builder()
                .tutorPerformances(tutorPerformances)
                .build();
    }
    
    private AdminAnalyticsDTO.StudentDistributionDTO calculateStudentDistributionForTutor(List<Course> courses) {
        long excellentStudents = 0;
        long goodStudents = 0;
        long averageStudents = 0;
        long weakStudents = 0;
        double totalScore = 0;
        long studentCount = 0;
        
        Set<Long> processedStudents = new HashSet<>();
        
        for (Course course : courses) {
            List<Enrollment> enrollments = course.getEnrollments() != null ? 
                    course.getEnrollments() : new ArrayList<>();
            
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getMentee() != null) {
                    Long menteeId = enrollment.getMentee().getId();
                    
                    if (!processedStudents.contains(menteeId)) {
                        processedStudents.add(menteeId);
                        
                        List<Submission> submissions = submissionRepository.findAll().stream()
                                .filter(s -> s.getMentee() != null && 
                                        s.getMentee().getId().equals(menteeId))
                                .collect(Collectors.toList());
                        
                        if (!submissions.isEmpty()) {
                            double avgScore = submissions.stream()
                                    .mapToDouble(s -> s.getGrade() != null ? s.getGrade() : 0)
                                    .average()
                                    .orElse(0);
                            
                            totalScore += avgScore;
                            studentCount++;
                            
                            if (avgScore >= 8.5) {
                                excellentStudents++;
                            } else if (avgScore >= 7) {
                                goodStudents++;
                            } else if (avgScore >= 5.5) {
                                averageStudents++;
                            } else {
                                weakStudents++;
                            }
                        }
                    }
                }
            }
        }
        
        double averageStudentScore = studentCount == 0 ? 0 : totalScore / studentCount;
        
        return AdminAnalyticsDTO.StudentDistributionDTO.builder()
                .excellentStudents(excellentStudents)
                .goodStudents(goodStudents)
                .averageStudents(averageStudents)
                .weakStudents(weakStudents)
                .averageStudentScore(averageStudentScore)
                .build();
    }
}
