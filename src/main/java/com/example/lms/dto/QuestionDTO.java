package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuestionDTO {
    private Long id;
    private Long menteeId;
    private Long forumId;

    private String content;
    private String answer;

    private LocalDateTime askedAt;
    private LocalDateTime answeredAt;
}