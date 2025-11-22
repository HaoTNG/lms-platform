package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementDTO {
    private Long id;
    private String title;
    private String content; // Maps to details in entity
    private String recipientType; // ALL, MENTEE, TUTOR, SPECIFIC
    private Long recipientUserId; // Only used when recipientType = SPECIFIC
    private LocalDateTime createdAt;
}
