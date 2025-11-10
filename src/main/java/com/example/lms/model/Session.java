package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Session
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
