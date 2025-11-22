package com.example.lms.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings", uniqueConstraints = {
        // Một Mentee chỉ được đánh giá 1 Session đúng 1 lần
        @UniqueConstraint(columnNames = {"mentee_id", "session_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentee_id", nullable = false)
    private Mentee mentee;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(nullable = false)
    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "reply_comment", columnDefinition = "TEXT")
    private String replyComment;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}