package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAnalyticsDTO {
    
    // ==================== SYSTEM STATISTICS ====================
    private SystemStatsDTO systemStats;
    
    // ==================== STUDENT ANALYTICS ====================
    private StudentAnalyticsDTO studentAnalytics;
    
    // ==================== TUTOR ANALYTICS ====================
    private TutorAnalyticsDTO tutorAnalytics;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SystemStatsDTO {
        // User statistics
        private Long totalUsers;
        private Long totalAdmins;
        private Long totalTutors;
        private Long totalMentees;
        
        // Course statistics
        private Long totalCourses;
        private Long activeCourses;
        private Long upcomingCourses;
        private Long finishedCourses;
        
        // Enrollment statistics
        private Long totalEnrollments;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentAnalyticsDTO {
        // Overall performance distribution
        private PerformanceDistributionDTO performanceDistribution;
        
        // Submission analysis
        private SubmissionAnalysisDTO submissionAnalysis;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PerformanceDistributionDTO {
        private Long excellentCount;      // >= 8.5
        private Long goodCount;           // >= 7 && < 8.5
        private Long averageCount;        // >= 5.5 && < 7
        private Long weakCount;           // < 5.5
        private Double averageScoreOverall;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SubmissionAnalysisDTO {
        private Long onTimeSubmissions;
        private Long lateSubmissions;
        private Double completionRate; // percentage
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TutorAnalyticsDTO {
        // Performance metrics per tutor
        private Map<String, TutorPerformanceDTO> tutorPerformances;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TutorPerformanceDTO {
        private Long tutorId;
        private String tutorName;
        private Double averageRating;
        private Long totalCourses;
        private Long completedCourses;
        private Double courseCompletionRate; // percentage
        private StudentDistributionDTO studentDistribution;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentDistributionDTO {
        private Long excellentStudents;
        private Long goodStudents;
        private Long averageStudents;
        private Long weakStudents;
        private Double averageStudentScore;
    }
}
