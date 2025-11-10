package com.example.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,Object> dto) { return ResponseEntity.ok().build(); }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> credentials) { return ResponseEntity.ok().build(); }
}
