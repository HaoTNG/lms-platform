package com.example.lms.service;

import com.example.lms.dto.CourseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CourseService {
    CourseDTO createCourse(CourseDTO dto);
    CourseDTO getCourseById(Long id);
    List<CourseDTO> getAllCourses();
    void enrollMentee(Long courseId, Long menteeId);
    void assignTutor(Long courseId, Long tutorId);
}
