package com.example.lms.mapper;

import com.example.lms.dto.ExerciseDTO;
import com.example.lms.model.Exercise;
import org.springframework.stereotype.Component;

@Component
public class ExerciseMapper {

    public ExerciseDTO toExerciseDTO(Exercise exercise) {
        if (exercise == null) {
            return null;
        }

        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(exercise.getId());
        dto.setLessonId(exercise.getLesson().getId());
        dto.setQuestion(exercise.getQuestion());
        dto.setDeadline(exercise.getDeadline());
        dto.setAttemptLimit(exercise.getAttemptLimit());

        dto.setSubmissionCount(
                exercise.getSubmissions() == null ? 0 : exercise.getSubmissions().size()
        );

        return dto;
    }

    public Exercise toExercise(ExerciseDTO dto) {
        if (dto == null) {
            return null;
        }

        Exercise ex = new Exercise();
        ex.setId(dto.getId());
        ex.setQuestion(dto.getQuestion());
        ex.setDeadline(dto.getDeadline());
        ex.setAttemptLimit(dto.getAttemptLimit());

        return ex;
    }
}