package com.example.lms.controller;

import com.example.lms.dto.CourseDTO;
import com.example.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
}
