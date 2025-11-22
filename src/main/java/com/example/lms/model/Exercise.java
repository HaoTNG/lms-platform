package com.example.lms.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "exercises")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(columnDefinition = "TEXT")
    private String question;

    private LocalDateTime deadline;

    private Integer attemptLimit;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<Submission> submissions;
}