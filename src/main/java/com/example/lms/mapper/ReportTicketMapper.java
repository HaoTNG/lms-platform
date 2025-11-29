package com.example.lms.mapper;

import com.example.lms.dto.ReportTicketDTO;
import com.example.lms.model.ReportTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReportTicketMapper {

    ReportTicketMapper INSTANCE = Mappers.getMapper(ReportTicketMapper.class);

    @Mapping(source = "mentee.id", target = "menteeId")
    @Mapping(source = "status", target = "status")
    ReportTicketDTO toDTO(ReportTicket reportTicket);


    @Mapping(source = "menteeId", target = "mentee.id")
    @Mapping(source = "status", target = "status")
    ReportTicket toEntity(ReportTicketDTO dto);
}
