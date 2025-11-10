package com.example.lms.service.interf;

import com.example.lms.dto.SubmissionDTO;
import org.springframework.stereotype.Service;

import java.util.List;


// may o lam` cac' service can thiet de sai nhu nay` de su dung, duoi day la example
@Service
public interface ExampleService {
    SubmissionDTO submitExercise(SubmissionDTO dto);
    SubmissionDTO getSubmission(Long id);
    List<SubmissionDTO> getSubmissionsByMentee(Long menteeId);
    List<SubmissionDTO> getSubmissionsByExercise(Long exerciseId);
}
