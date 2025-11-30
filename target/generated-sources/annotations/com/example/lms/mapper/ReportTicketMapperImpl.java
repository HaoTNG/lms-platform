package com.example.lms.mapper;

import com.example.lms.dto.ReportTicketDTO;
import com.example.lms.enums.ReportTicketStatus;
import com.example.lms.model.Mentee;
import com.example.lms.model.ReportTicket;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
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
        reportTicketDTO.setId( reportTicket.getId() );
        reportTicketDTO.setTitle( reportTicket.getTitle() );
        reportTicketDTO.setDescription( reportTicket.getDescription() );
        reportTicketDTO.setAdminResponse( reportTicket.getAdminResponse() );
        reportTicketDTO.setCreatedAt( reportTicket.getCreatedAt() );
        reportTicketDTO.setResolvedAt( reportTicket.getResolvedAt() );

        return reportTicketDTO;
    }

    @Override
    public ReportTicket toEntity(ReportTicketDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ReportTicket reportTicket = new ReportTicket();

        reportTicket.setMentee( reportTicketDTOToMentee( dto ) );
        if ( dto.getStatus() != null ) {
            reportTicket.setStatus( Enum.valueOf( ReportTicketStatus.class, dto.getStatus() ) );
        }
        reportTicket.setId( dto.getId() );
        reportTicket.setTitle( dto.getTitle() );
        reportTicket.setDescription( dto.getDescription() );
        reportTicket.setAdminResponse( dto.getAdminResponse() );
        reportTicket.setCreatedAt( dto.getCreatedAt() );
        reportTicket.setResolvedAt( dto.getResolvedAt() );

        return reportTicket;
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
