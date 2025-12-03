package com.example.lms.dto;

import com.example.lms.enums.ResourceType;
import lombok.Data;

@Data
public class ResourceDTO {
    private Long id;
    private Long lessonId;
    private String title;
    private String fileUrl;
    private ResourceType resourceType;
}