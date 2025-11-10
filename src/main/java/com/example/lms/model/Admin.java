package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Admin
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
