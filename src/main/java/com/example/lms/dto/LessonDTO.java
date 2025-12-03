package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonDTO {
    private Long id;
    private Long courseId;
    private String title;
    private String description;

    private List<Long> exerciseIds;
    private List<Long> resourceIds;
    private int exerciseCount;
    private int resourceCount;
}
