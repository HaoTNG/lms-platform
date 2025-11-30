package com.example.lms.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LessonDTO {
    private Long id;
    private Long courseId;
    private String title;
    private String description;

    private List<Long> exerciseIds;
    private List<Long> resourceIds;
}
