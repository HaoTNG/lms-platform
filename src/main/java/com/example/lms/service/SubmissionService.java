package com.example.lms.service;

import com.example.lms.dto.SubmissionDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface SubmissionService {
    SubmissionDTO submitExercise(SubmissionDTO dto);
    SubmissionDTO getSubmission(Long id);
    List<SubmissionDTO> getSubmissionsByMentee(Long menteeId);
    List<SubmissionDTO> getSubmissionsByExercise(Long exerciseId);
}
