package com.example.lms.mapper;

import com.example.lms.dto.LessonDTO;
import com.example.lms.model.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    public LessonDTO toLessonDTO(Lesson lesson) {
        if (lesson == null) {
            return null;
        }

        LessonDTO dto = new LessonDTO();

        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setDescription(lesson.getDescription());

        dto.setExerciseCount(
                lesson.getExercises() == null ? 0 : lesson.getExercises().size()
        );

        dto.setResourceCount(
                lesson.getResources() == null ? 0 : lesson.getResources().size()
        );

        return dto;
    }

    public Lesson toLesson(LessonDTO dto) {
        if (dto == null) {
            return null;
        }

        Lesson lesson = new Lesson();
        lesson.setId(dto.getId());
        lesson.setTitle(dto.getTitle());
        lesson.setDescription(dto.getDescription());

        return lesson;
    }
}
