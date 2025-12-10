package com.example.lms.dto;

import com.example.lms.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceDTO {
    private Long id;
    private Long lessonId;
    private String title;
    private String fileUrl;
    private String resourceType;
}