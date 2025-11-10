package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Resource
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
