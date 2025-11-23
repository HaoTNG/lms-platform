package com.example.lms.service.interf;

import com.example.lms.dto.AdminAnalyticsDTO;

public interface AdminAnalyticsService {
    AdminAnalyticsDTO getAllAnalytics();
    AdminAnalyticsDTO.SystemStatsDTO getSystemStatistics();
    AdminAnalyticsDTO.StudentAnalyticsDTO getStudentAnalytics();
    AdminAnalyticsDTO.TutorAnalyticsDTO getTutorAnalytics();
}
