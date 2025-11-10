package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Mentee
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Mentee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
