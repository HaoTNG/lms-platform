package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RatingDTO {
    private Long id;
    private Long menteeId;
    private Long sessionId;
    private Integer score;
    private String comment;
    private String replyComment;
    private LocalDateTime createdAt;

}
