package com.example.lms.mapper;

import com.example.lms.dto.AnnouncementDTO;
import com.example.lms.model.Announcement;
import com.example.lms.model.Admin;
import com.example.lms.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {

    @Mapping(target = "adminId", source = "admin.id")
    @Mapping(target = "recipientUserId", source = "recipientUser.userId")
    AnnouncementDTO toDTO(Announcement announcement);

    // TRƯỜNG HỢP NGƯỢC LẠI: chỉ map phần đơn giản
    @Mapping(target = "admin", ignore = true)
    @Mapping(target = "recipientUser", ignore = true)
    Announcement toEntity(AnnouncementDTO dto);
}
