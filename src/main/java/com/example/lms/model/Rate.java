package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Rate
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
