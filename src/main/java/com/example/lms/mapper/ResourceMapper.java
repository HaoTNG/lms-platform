package com.example.lms.mapper;

import com.example.lms.dto.ResourceDTO;
import com.example.lms.enums.ResourceType;
import com.example.lms.model.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {

    public ResourceDTO toResourceDTO(Resource resource) {
        if (resource == null) {
            return null;
        }

        ResourceDTO dto = new ResourceDTO();
        dto.setId(resource.getId());
        dto.setLessonId(resource.getLesson().getId());
        dto.setTitle(resource.getTitle());
        dto.setFileUrl(resource.getFileUrl());
        dto.setResourceType(resource.getResourceType().toString());

        return dto;
    }

    public Resource toResource(ResourceDTO dto) {
        if (dto == null) {
            return null;
        }

        Resource resource = new Resource();
        resource.setId(dto.getId());
        resource.setTitle(dto.getTitle());
        resource.setFileUrl(dto.getFileUrl());
        resource.setResourceType(ResourceType.fromValue(dto.getResourceType()));

        return resource;
    }
}
