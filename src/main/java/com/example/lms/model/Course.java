package com.example.lms.model;

import com.example.lms.enums.CourseStatus;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "course_status")
    private CourseStatus course_status;
    private LocalDate start_date;
    private LocalDate end_date;
    private String tutor;

}
