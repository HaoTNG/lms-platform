package com.example.lms.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.example.lms.model.Submission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO {
    private Long id;
    private Long lessonId;
    private String question;
    private LocalDateTime deadline;
    private Integer attemptLimit;
    private List<Submission> submissions;
    private int submissionCount;
}
