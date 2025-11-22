package com.example.lms.model;

import com.example.lms.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Placeholder entity: Course
 * Fields and relations to be implemented by the team.
 */
@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @OneToOne
    @JoinColumn(name = "registration_id", nullable = false)
    private SubjectRegistration subjectRegistration;

    @Column(nullable = false)
    private String courseName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long maxMentee;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_status")
    @Builder.Default
    private CourseStatus courseStatus = CourseStatus.OPEN;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Session> sessions;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        if (this.courseStatus == null) {
            this.courseStatus = CourseStatus.OPEN;
        }
    }
}