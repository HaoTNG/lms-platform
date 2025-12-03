package com.example.lms.dto;

import lombok.Data;

@Data
public class CreateRatingRequest {
    private Integer score;
    private String comment;
}