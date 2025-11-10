package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Submission
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
