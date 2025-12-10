package com.example.lms.mapper;

import com.example.lms.dto.SubmissionDTO;
import com.example.lms.model.Exercise;
import com.example.lms.model.Mentee;
import com.example.lms.model.Submission;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {
    public SubmissionDTO toSubmissionDTO(Submission submission) {
        if (submission == null) return null;

        SubmissionDTO dto = new SubmissionDTO();

        dto.setId(submission.getId());
        dto.setTextAnswer(submission.getTextAnswer());
        dto.setFileUrl(submission.getFileUrl());
        dto.setGrade(submission.getGrade());

        if (submission.getExercise() != null) {
            dto.setExerciseId(submission.getExercise().getId());
        }

        if (submission.getMentee() != null) {
            dto.setMenteeId(submission.getMentee().getId());
        }

        return dto;
    }

    public Submission toSubmission(SubmissionDTO dto) {
        if (dto == null) return null;

        Submission submission = Submission.builder()
                .id(dto.getId())
                .textAnswer(dto.getTextAnswer())
                .fileUrl(dto.getFileUrl())
                .grade(dto.getGrade())
                .build();

        if (dto.getExerciseId() != null) {
            Exercise ex = new Exercise();
            ex.setId(dto.getExerciseId());
            submission.setExercise(ex);
        }

        if (dto.getMenteeId() != null) {
            Mentee mentee = new Mentee();
            mentee.setId(dto.getMenteeId());
            submission.setMentee(mentee);
        }

        return submission;
    }
}
