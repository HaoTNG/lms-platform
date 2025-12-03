package com.example.lms.mapper;

import com.example.lms.dto.MessageDTO;
import com.example.lms.model.Conversation;
import com.example.lms.model.Message;
import com.example.lms.model.User;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageDTO toMessageDTO(Message message) {
        if (message == null) return null;

        MessageDTO dto = new MessageDTO();

        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setRead(message.isRead());
        dto.setSentAt(message.getSentAt());

        if (message.getConversation() != null) {
            dto.setConversationId(message.getConversation().getId());
        }

        if (message.getSender() != null) {
            dto.setSenderId(message.getSender().getId());
        }

        if (message.getReceiver() != null) {
            dto.setReceiverId(message.getReceiver().getId());
        }

        return dto;
    }

    public Message toMessage(MessageDTO dto) {
        if (dto == null) return null;

        Message m = new Message();
        m.setId(dto.getId());
        m.setContent(dto.getContent());
        m.setRead(dto.isRead());
        m.setSentAt(dto.getSentAt());

        return m;
    }
}
