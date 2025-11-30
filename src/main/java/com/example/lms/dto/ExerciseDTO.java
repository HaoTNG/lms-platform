package com.example.lms.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.example.lms.model.Submission;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseDTO {
    private Long id;
    private Long lessonId;
    private String question;
    private LocalDateTime deadline;
    private Integer attemptLimit;
    private List<Submission> submissions;
}
