package com.example.lms.service.interf;

import com.example.lms.dto.AdminAnalyticsDTO;
import com.example.lms.dto.Response;

public interface AdminAnalyticsService {
    Response getAllAnalytics();
    AdminAnalyticsDTO.SystemStatsDTO getSystemStatistics();
    AdminAnalyticsDTO.StudentAnalyticsDTO getStudentAnalytics();
    AdminAnalyticsDTO.TutorAnalyticsDTO getTutorAnalytics();
}
