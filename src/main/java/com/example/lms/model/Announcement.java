package com.example.lms.model;

import jakarta.persistence.*;

/**
 * Placeholder entity: Announcement
 * Fields and relations to be implemented by the team.
 */
@Entity
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
