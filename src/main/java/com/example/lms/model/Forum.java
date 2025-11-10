package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Forum
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
