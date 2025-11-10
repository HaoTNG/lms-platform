package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Subject
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
