package com.example.lms.controller;



import com.example.lms.dto.Response;
import com.example.lms.dto.UserDTO;
import com.example.lms.service.interf.AdminService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/manage-user")
    public ResponseEntity<Response> getUserList() {
        Response response = adminService.ManageUser();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
