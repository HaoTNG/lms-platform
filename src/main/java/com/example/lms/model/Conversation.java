package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Conversation
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
