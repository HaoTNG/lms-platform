package com.example.lms.mapper;

import com.example.lms.dto.QuestionDTO;
import com.example.lms.model.Forum;
import com.example.lms.model.Mentee;
import com.example.lms.model.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public QuestionDTO toQuestionDTO(Question q) {
        if (q == null) return null;

        QuestionDTO dto = new QuestionDTO();
        dto.setId(q.getId());
        dto.setContent(q.getContent());
        dto.setAnswer(q.getAnswer());
        dto.setAskedAt(q.getAskedAt());
        dto.setAnsweredAt(q.getAnsweredAt());

        if (q.getMentee() != null) {
            dto.setMenteeId(q.getMentee().getId());
        }

        if (q.getForum() != null) {
            dto.setForumId(q.getForum().getId());
        }

        return dto;
    }

    public Question toQuestion(QuestionDTO dto) {
        if (dto == null) return null;

        Question q = new Question();
        q.setId(dto.getId());
        q.setContent(dto.getContent());
        q.setAnswer(dto.getAnswer());
        q.setAskedAt(dto.getAskedAt());
        q.setAnsweredAt(dto.getAnsweredAt());

        return q;
    }
}
