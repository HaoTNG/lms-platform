package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Enrollment
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
