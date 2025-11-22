package com.example.lms.model;

import com.example.lms.enums.SessionType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Placeholder entity: Session
 * Fields and relations to be implemented by the team.
 */
@Entity
@Table(name = "sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    @Enumerated(EnumType.STRING)
    private SessionType type;

    private String room;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private Forum forum;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    // Helper function to calculate average rating
    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) return 0.0;
        return ratings.stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
    }
}