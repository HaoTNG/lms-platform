package com.example.lms.mapper;

import com.example.lms.dto.RatingDTO;
import com.example.lms.model.Mentee;
import com.example.lms.model.Rating;
import com.example.lms.model.Session;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    // =============== ENTITY → DTO ===============
    public RatingDTO toRatingDTO(Rating rating) {
        if (rating == null) return null;

        RatingDTO dto = new RatingDTO();
        dto.setId(rating.getId());
        dto.setScore(rating.getScore());
        dto.setComment(rating.getComment());
        dto.setReplyComment(rating.getReplyComment());
        dto.setCreatedAt(rating.getCreatedAt());

        if (rating.getMentee() != null) {
            dto.setMenteeId(rating.getMentee().getId());
        }

        if (rating.getSession() != null) {
            dto.setSessionId(rating.getSession().getId());
        }

        return dto;
    }

    public Rating toRating(RatingDTO dto) {
        if (dto == null) return null;

        Rating rating = new Rating();
        rating.setId(dto.getId());
        rating.setScore(dto.getScore());
        rating.setComment(dto.getComment());
        rating.setReplyComment(dto.getReplyComment());
        rating.setCreatedAt(dto.getCreatedAt()); // khi update hoặc admin reply

        return rating;
    }
}
