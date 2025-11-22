package com.example.lms.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * Placeholder entity: Lesson
 * Fields and relations to be implemented by the team.
 */
@Entity
@Table(name = "lessons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Exercise> exercises;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Resource> resources;
}
