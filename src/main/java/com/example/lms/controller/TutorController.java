package com.example.lms.controller;

import com.example.lms.service.interf.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tutors")
@RequiredArgsConstructor
public class TutorController {
    private final TutorService tutorService;
    //cac api
}
