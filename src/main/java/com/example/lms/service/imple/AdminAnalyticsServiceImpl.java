package com.example.lms.service.imple;

import com.example.lms.dto.AdminAnalyticsDTO;
import com.example.lms.dto.Response;
import com.example.lms.enums.CourseStatus;
import com.example.lms.model.*;
import com.example.lms.repository.*;
import com.example.lms.service.interf.AdminAnalyticsService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.*;
@Service
@RequiredArgsConstructor
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SubmissionRepository submissionRepository;
    private final SessionRepository sessionRepository;

    @Override
    public Response getAllAnalytics() {
        try {
            var dto = AdminAnalyticsDTO.builder()
                    .systemStats(getSystemStatistics())
                    .studentAnalytics(getStudentAnalytics())
                    .tutorAnalytics(getTutorAnalytics())
                    .build();

            return Response.builder()
                    .statusCode(200)
                    .message("Fetched all analytics successfully")
                    .data(dto)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Failed to fetch analytics: " + e.getMessage())
                    .build();
        }
    }


    @Override
    public AdminAnalyticsDTO.SystemStatsDTO getSystemStatistics() {

        return AdminAnalyticsDTO.SystemStatsDTO.builder()
                .totalUsers(userRepository.countTotalUsers())
                .totalAdmins(userRepository.countAdmins())
                .totalTutors(userRepository.countTutors())
                .totalMentees(userRepository.countMentees())
                .totalCourses(courseRepository.countTotalCourses())
                .activeCourses(courseRepository.countActiveCourses())
                .finishedCourses(courseRepository.countFinishedCourses())
                .totalEnrollments(Optional.ofNullable(courseRepository.countTotalEnrollments()).orElse(0L))
                .build();
    }



    @Override
    public AdminAnalyticsDTO.StudentAnalyticsDTO getStudentAnalytics() {

        List<Object[]> avgScoreList = submissionRepository.getAverageScoreByMentee();

        long excellent = 0, good = 0, average = 0, weak = 0;
        double totalScore = 0;

        for (Object[] row : avgScoreList) {
            double score = (Double) row[1];
            totalScore += score;

            if (score >= 8.5) excellent++;
            else if (score >= 7) good++;
            else if (score >= 5.5) average++;
            else weak++;
        }

        double averageScoreOverall = avgScoreList.isEmpty()
                ? 0
                : totalScore / avgScoreList.size();

        long onTime = submissionRepository.countOnTime();
        long late = submissionRepository.countLate();
        long total = submissionRepository.countTotal();
        double completionRate = total == 0 ? 0 : (onTime + late) * 100.0 / total;

        return AdminAnalyticsDTO.StudentAnalyticsDTO.builder()
                .performanceDistribution(AdminAnalyticsDTO.PerformanceDistributionDTO.builder()
                        .excellentCount(excellent)
                        .goodCount(good)
                        .averageCount(average)
                        .weakCount(weak)
                        .averageScoreOverall(averageScoreOverall)
                        .build())
                .submissionAnalysis(AdminAnalyticsDTO.SubmissionAnalysisDTO.builder()
                        .onTimeSubmissions(onTime)
                        .lateSubmissions(late)
                        .completionRate(completionRate)
                        .build())
                .build();
    }


    @Override
    public AdminAnalyticsDTO.TutorAnalyticsDTO getTutorAnalytics() {
        List<Session> sessions = sessionRepository.findAll();

        double avgRating = sessions.stream()
                .mapToDouble(Session::getAverageRating)
                .average()
                .orElse(0);
        List<Course> courses = courseRepository.findAll();

        long totalCourses = courses.size();
        long completedCourses = courses.stream()
                .filter(c -> c.getCourseStatus() == CourseStatus.END)
                .count();

        double completionRate = totalCourses == 0
                ? 0
                : completedCourses * 100.0 / totalCourses;

        return AdminAnalyticsDTO.TutorAnalyticsDTO.builder()
                .averageRating(avgRating)
                .courseCompletionRate(completionRate)
                .build();
    }
}
