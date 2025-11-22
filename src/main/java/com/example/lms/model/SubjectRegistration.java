package com.example.lms.model;

import com.example.lms.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Placeholder entity: SubjectRegistration
 * Fields and relations to be implemented by the team.
 */
@Entity
@Table(name = "subject_registrations")
@Data
public class SubjectRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @OneToOne(mappedBy = "subjectRegistration", cascade = CascadeType.ALL)
    private Course course;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus = RegistrationStatus.PENDING;

    private LocalDateTime registeredAt;

    @PrePersist
    protected void onCreate() {
        this.registeredAt = LocalDateTime.now();
        if (this.registrationStatus == null) {
            this.registrationStatus = RegistrationStatus.PENDING;
        }
    }
}