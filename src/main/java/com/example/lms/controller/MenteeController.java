package com.example.lms.controller;

import com.example.lms.service.interf.MenteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mentees")
@RequiredArgsConstructor
public class MenteeController {
    private final MenteeService menteeService;
    //cac api
}
