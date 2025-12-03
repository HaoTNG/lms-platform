package com.example.lms.mapper;

import com.example.lms.dto.ConversationDTO;
import com.example.lms.model.Conversation;
import org.springframework.stereotype.Component;

@Component
public class ConversationMapper {

    public ConversationDTO toConversationDTO(Conversation c) {
        if (c == null) return null;

        ConversationDTO dto = new ConversationDTO();
        dto.setId(c.getId());
        dto.setLastMessageAt(c.getLastMessageAt());

        if (c.getMentee() != null) {
            dto.setMenteeId(c.getMentee().getId());
        }

        if (c.getTutor() != null) {
            dto.setTutorId(c.getTutor().getId());
        }

        return dto;
    }

    public Conversation toConversation(ConversationDTO dto) {
        if (dto == null) return null;

        Conversation c = new Conversation();
        c.setId(dto.getId());
        c.setLastMessageAt(dto.getLastMessageAt());

        return c;
    }
}