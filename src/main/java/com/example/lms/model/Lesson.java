package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Lesson
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
