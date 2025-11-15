package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportTicketDTO {
    private Long id;
    private Long menteeId;
    private String title;
    private String description;
    private String status; // PENDING, RESOLVED, REJECTED
    private String adminResponse;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
