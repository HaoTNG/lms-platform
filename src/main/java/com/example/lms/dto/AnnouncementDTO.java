package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnnouncementDTO {
    private Long id;
    private Long adminId;
    private String title;
    private String content;
    private String recipientType; // ALL, MENTEE, TUTOR, SPECIFIC
    private Long recipientUserId; // Dùng nếu recipientType = SPECIFIC
    private LocalDateTime createdAt;
    private Boolean isRead;
}
