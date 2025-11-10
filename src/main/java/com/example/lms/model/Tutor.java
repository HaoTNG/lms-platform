package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Tutor
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
