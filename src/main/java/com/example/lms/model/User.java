package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: User
 * Fields and relations to be implemented by the team.
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
