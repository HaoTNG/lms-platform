package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Course
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String course_name;
    private Long max_no_mentee;
    private String course_status;


}
