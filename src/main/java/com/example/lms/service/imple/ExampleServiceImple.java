package com.example.lms.service.imple;

import com.example.lms.dto.SubmissionDTO;
import com.example.lms.service.interf.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


// noi implement cac service
@Service
@RequiredArgsConstructor
public class ExampleServiceImple implements ExampleService {
    SubmissionDTO submitExercise(SubmissionDTO dto){
        // implement o day
    };
    SubmissionDTO getSubmission(Long id){

    };
    List<SubmissionDTO> getSubmissionsByMentee(Long menteeId){

    };
    List<SubmissionDTO> getSubmissionsByExercise(Long exerciseId){

    };
}
