package com.example.lms.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubmissionDTO {
    private Long id;
    private Long exerciseId;
    private Long menteeId;
    private String textAnswer;
    private String fileUrl;

}