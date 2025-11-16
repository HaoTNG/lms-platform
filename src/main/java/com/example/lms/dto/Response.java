package com.example.lms.dto;

import com.example.lms.model.Course;
import com.example.lms.model.Mentee;
import com.example.lms.model.Tutor;
import com.example.lms.model.ReportTicket;
import com.example.lms.model.Announcement;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    int statusCode;
    String message;

    Object data;
    Pagination pagination;
    UserDTO user;
    Course course;
    Tutor tutor;

    List<UserDTO> userList;
    List<TutorDTO> tutorLIst;
    List<MenteeDTO> menteeList;
    List<Course> courseList;
    
    // Report Ticket fields
    ReportTicket reportTicket;
    List<ReportTicket> reportTicketList;
    
    // Announcement fields
    Announcement announcement;
    AnnouncementDTO announcementDTO;
    List<Announcement> announcementList;
    List<AnnouncementDTO> announcementListDTO;
}
