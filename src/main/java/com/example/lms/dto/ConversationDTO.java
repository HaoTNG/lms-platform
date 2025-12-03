package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConversationDTO {
    private Long id;
    private Long menteeId;
    private Long tutorId;
    private LocalDateTime lastMessageAt;
}
