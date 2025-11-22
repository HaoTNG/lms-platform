package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**
 * Placeholder entity: Subject
 * Fields and relations to be implemented by the team.
 */
@Entity
@Table(name = "subjects")
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String subjectName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<SubjectRegistration> registrations;
}