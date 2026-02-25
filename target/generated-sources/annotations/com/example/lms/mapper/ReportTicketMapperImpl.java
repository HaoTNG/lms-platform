package com.example.lms.mapper;

import com.example.lms.dto.ReportTicketDTO;
import com.example.lms.enums.ReportTicketStatus;
import com.example.lms.model.Mentee;
import com.example.lms.model.ReportTicket;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ReportTicketMapperImpl implements ReportTicketMapper {

    @Override
    public ReportTicketDTO toDTO(ReportTicket reportTicket) {
        if ( reportTicket == null ) {
            return null;
        }

        ReportTicketDTO reportTicketDTO = new ReportTicketDTO();

        reportTicketDTO.setMenteeId( reportTicketMenteeId( reportTicket ) );
        if ( reportTicket.getStatus() != null ) {
            reportTicketDTO.setStatus( reportTicket.getStatus().name() );
        }
        reportTicketDTO.setAdminResponse( reportTicket.getAdminResponse() );
        reportTicketDTO.setCreatedAt( reportTicket.getCreatedAt() );
        reportTicketDTO.setDescription( reportTicket.getDescription() );
        reportTicketDTO.setId( reportTicket.getId() );
        reportTicketDTO.setResolvedAt( reportTicket.getResolvedAt() );
        reportTicketDTO.setTitle( reportTicket.getTitle() );

        return reportTicketDTO;
    }

    @Override
    public ReportTicket toEntity(ReportTicketDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ReportTicket.ReportTicketBuilder reportTicket = ReportTicket.builder();

        reportTicket.mentee( reportTicketDTOToMentee( dto ) );
        if ( dto.getStatus() != null ) {
            reportTicket.status( Enum.valueOf( ReportTicketStatus.class, dto.getStatus() ) );
        }
        reportTicket.adminResponse( dto.getAdminResponse() );
        reportTicket.createdAt( dto.getCreatedAt() );
        reportTicket.description( dto.getDescription() );
        reportTicket.id( dto.getId() );
        reportTicket.resolvedAt( dto.getResolvedAt() );
        reportTicket.title( dto.getTitle() );

        return reportTicket.build();
    }

    private Long reportTicketMenteeId(ReportTicket reportTicket) {
        if ( reportTicket == null ) {
            return null;
        }
        Mentee mentee = reportTicket.getMentee();
        if ( mentee == null ) {
            return null;
        }
        Long id = mentee.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Mentee reportTicketDTOToMentee(ReportTicketDTO reportTicketDTO) {
        if ( reportTicketDTO == null ) {
            return null;
        }

        Mentee mentee = new Mentee();

        mentee.setId( reportTicketDTO.getMenteeId() );

        return mentee;
    }
}
