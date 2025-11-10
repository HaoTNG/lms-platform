package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Question
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
