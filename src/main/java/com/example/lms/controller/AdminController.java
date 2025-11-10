package com.example.lms.controller;


import com.example.lms.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    //cac api o day
}
