package com.example.lms.mapper;

import com.example.lms.dto.SessionDTO;
import com.example.lms.model.Forum;
import com.example.lms.model.Session;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public SessionDTO toSessionDTO(Session session) {
        if (session == null) return null;

        SessionDTO dto = new SessionDTO();

        dto.setId(session.getId());
        dto.setCourseId(session.getCourse().getCourseId());
        dto.setType(session.getType());
        dto.setRoom(session.getRoom());
        dto.setDate(session.getDate());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());

        dto.setAverageRating(session.getAverageRating());
        dto.setRatingCount(
                session.getRatings() == null ? 0 : session.getRatings().size()
        );

        Forum forum = session.getForum();
        if (forum != null) {
            dto.setForumId(forum.getId());
        }

        return dto;
    }

    public Session toSession(SessionDTO dto) {
        if (dto == null) return null;

        Session session = new Session();

        session.setId(dto.getId());
        session.setType(dto.getType());
        session.setRoom(dto.getRoom());
        session.setDate(dto.getDate());
        session.setStartTime(dto.getStartTime());
        session.setEndTime(dto.getEndTime());

        return session;
    }
}
