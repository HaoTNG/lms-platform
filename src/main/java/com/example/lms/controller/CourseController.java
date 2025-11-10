package com.example.lms.controller;

import com.example.lms.dto.CourseDTO;
import com.example.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    @Autowired
    private final CourseService courseService;
}
