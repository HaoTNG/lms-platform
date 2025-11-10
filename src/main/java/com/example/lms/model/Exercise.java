package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Exercise
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
