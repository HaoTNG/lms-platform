package com.example.lms.dto;

import com.example.lms.model.Mentee;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    int statusCode;
    String message;
    UserDTO user;

    List<UserDTO> userList;
    List<TutorDTO> tutorLIst;
    List<MenteeDTO> menteeList;


}
