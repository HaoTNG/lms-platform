package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
@Data
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    private String title;
    private String content;
    private String recipientType; // ALL, MENTEE, TUTOR, SPECIFIC
    
    @ManyToOne
    @JoinColumn(name = "recipient_user_id")
    private User recipientUser; // Dùng nếu recipientType = SPECIFIC

    private LocalDateTime createdAt;
    private Boolean isRead = false;
}
