package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Grade
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
