package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Long courseId;
    private String courseName;
    private String description;
    private Long maxMentee;
    private String courseStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdDate;
    private Long subjectRegistrationId;  // Only ID, not full object
    private String tutorName;             // Tutor info as simple fields
    private Long tutorId;
    private int totalLessons;
    private int totalSessions;
}