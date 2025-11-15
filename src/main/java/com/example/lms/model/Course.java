package com.example.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Placeholder entity: Course
 * Fields and relations to be implemented by the team.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String course_name;
    private Long max_no_mentee;
    private String course_status;
    private LocalDate start_date;
    private LocalDate end_date;
    private String tutor;

}
