package com.example.lms.mapper;

import com.example.lms.dto.AnnouncementDTO;
import com.example.lms.model.Announcement;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementMapper {

    public AnnouncementDTO toDTO(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        
        return AnnouncementDTO.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getDetails())
                .recipientType(announcement.getRecipientType())
                .createdAt(announcement.getCreatedAt())
                .build();
    }

    public Announcement toEntity(AnnouncementDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // Note: sender and announcementUsers must be set separately
        // This mapper does not handle relationships
        return Announcement.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .details(dto.getContent())
                .recipientType(dto.getRecipientType())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
