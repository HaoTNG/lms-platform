package com.example.lms.mapper;

import com.example.lms.dto.AnnouncementDTO;
import com.example.lms.model.Admin;
import com.example.lms.model.Announcement;
import com.example.lms.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class AnnouncementMapperImpl implements AnnouncementMapper {

    @Override
    public AnnouncementDTO toDTO(Announcement announcement) {
        if ( announcement == null ) {
            return null;
        }

        AnnouncementDTO.AnnouncementDTOBuilder announcementDTO = AnnouncementDTO.builder();

        announcementDTO.adminId( announcementAdminId( announcement ) );
        announcementDTO.recipientUserId( announcementRecipientUserUserId( announcement ) );
        announcementDTO.content( announcement.getContent() );
        announcementDTO.createdAt( announcement.getCreatedAt() );
        announcementDTO.id( announcement.getId() );
        announcementDTO.isRead( announcement.getIsRead() );
        announcementDTO.recipientType( announcement.getRecipientType() );
        announcementDTO.title( announcement.getTitle() );

        return announcementDTO.build();
    }

    @Override
    public Announcement toEntity(AnnouncementDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Announcement.AnnouncementBuilder announcement = Announcement.builder();

        announcement.content( dto.getContent() );
        announcement.createdAt( dto.getCreatedAt() );
        announcement.id( dto.getId() );
        announcement.isRead( dto.getIsRead() );
        announcement.recipientType( dto.getRecipientType() );
        announcement.title( dto.getTitle() );

        return announcement.build();
    }

    private Long announcementAdminId(Announcement announcement) {
        if ( announcement == null ) {
            return null;
        }
        Admin admin = announcement.getAdmin();
        if ( admin == null ) {
            return null;
        }
        Long id = admin.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long announcementRecipientUserUserId(Announcement announcement) {
        if ( announcement == null ) {
            return null;
        }
        User recipientUser = announcement.getRecipientUser();
        if ( recipientUser == null ) {
            return null;
        }
        Long userId = recipientUser.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
