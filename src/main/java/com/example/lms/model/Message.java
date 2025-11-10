package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Message
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
