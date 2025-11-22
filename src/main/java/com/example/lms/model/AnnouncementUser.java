package com.example.lms.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcement_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;


    @Builder.Default
    private Boolean isRead = false;
    
    private LocalDateTime readAt; 
}