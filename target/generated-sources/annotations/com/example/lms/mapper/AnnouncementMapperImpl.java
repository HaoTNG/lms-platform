package com.example.lms.mapper;

import com.example.lms.dto.AnnouncementDTO;
import com.example.lms.model.Admin;
import com.example.lms.model.Announcement;
import com.example.lms.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Arch Linux)"
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
        announcementDTO.id( announcement.getId() );
        announcementDTO.title( announcement.getTitle() );
        announcementDTO.content( announcement.getContent() );
        announcementDTO.recipientType( announcement.getRecipientType() );
        announcementDTO.createdAt( announcement.getCreatedAt() );
        announcementDTO.isRead( announcement.getIsRead() );

        return announcementDTO.build();
    }

    @Override
    public Announcement toEntity(AnnouncementDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Announcement.AnnouncementBuilder announcement = Announcement.builder();

        announcement.id( dto.getId() );
        announcement.title( dto.getTitle() );
        announcement.content( dto.getContent() );
        announcement.recipientType( dto.getRecipientType() );
        announcement.createdAt( dto.getCreatedAt() );
        announcement.isRead( dto.getIsRead() );

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
