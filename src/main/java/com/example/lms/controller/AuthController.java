package com.example.lms.controller;

import com.example.lms.dto.LoginRequest;
import com.example.lms.dto.RegisterRequest;
import com.example.lms.dto.Response;
import com.example.lms.dto.UserDTO;
import com.example.lms.mapper.UserMapper;
import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import com.example.lms.security.JwtUserDetails;
import com.example.lms.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;


    /**
     * Register - Đăng ký tài khoản mới
     */
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest userDTO, HttpServletResponse response) {
        Response res = new Response();
        try {
            // Kiểm tra email đã tồn tại
            boolean exists = userRepository.existsByEmail(userDTO.getEmail());
            if (exists) {
                res.setStatusCode(400);
                res.setMessage("Email already exists");
                return ResponseEntity.status(400).body(res);
            }

            // Tạo user mới
            User newUser = new User();
            newUser.setName(userDTO.getName());
            newUser.setEmail(userDTO.getEmail());
            newUser.setPassword_hashed(passwordEncoder.encode(userDTO.getPassword_hashed()));
            newUser.setRole(userDTO.getRole() != null ? userDTO.getRole() : "MENTEE");

            User savedUser = userRepository.save(newUser);

            // Tạo JWT token
            String token = jwtUtil.generateToken(
                    savedUser.getUserId().toString(),
                    savedUser.getEmail(),
                    savedUser.getRole()
            );
            System.out.println(token);
            userDTO.setUserId(savedUser.getUserId());

            // Set token vào cookie
            ResponseCookie cookie = ResponseCookie.from("Authorization", token)
                    .httpOnly(true)
                    .secure(false) // true khi production HTTPS
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .build();

            res.setStatusCode(200);
            res.setMessage("User registered successfully");
            res.setUser(userMapper.toUserDTO(newUser));

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.setStatusCode(500);
            res.setMessage("Error registering user: " + e.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }



    /**
     * Login - Đăng nhập
     */
    @PostMapping("/login")
    public ResponseEntity<Response> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {

        Response res = new Response();

        // Tìm user bằng email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword_hashed(), user.getPassword_hashed())) {
            res.setStatusCode(401);
            res.setMessage("Invalid email or password");
            return ResponseEntity.status(401).body(res);
        }

        String token = jwtUtil.generateToken(
                user.getUserId().toString(),
                user.getEmail(),
                user.getRole()
        );

        // Set cookie chuẩn, bảo mật tốt hơn
        ResponseCookie cookie = ResponseCookie.from("Authorization", token)
                .httpOnly(true)
                .secure(false) // true khi production HTTPS
                .sameSite("Strict")
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());

        res.setStatusCode(200);
        res.setMessage("Login successful");
        res.setUser(userDTO);
        return ResponseEntity.ok(res);
    }


    /**
     * Logout - Đăng xuất
     */
    @PostMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletResponse response) {

        Response res = new Response();

        ResponseCookie cookie = ResponseCookie.from("Authorization", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(0) // expire immediately
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        res.setStatusCode(200);
        res.setMessage("Logout successful");

        return ResponseEntity.ok(res);
    }


    /**
     * Check authentication status
     */
    @GetMapping("/me")
    public ResponseEntity<Response> getCurrentUser() {
        Response res = new Response();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            res.setStatusCode(401);
            res.setMessage("Unauthorized");
            System.out.println("a");
            return ResponseEntity.status(401).body(res);

        }

        Object principal = auth.getPrincipal();

        if (!(principal instanceof JwtUserDetails jwtUser)) {
            res.setStatusCode(401);
            res.setMessage("Unauthorized");
            System.out.println("b");
            return ResponseEntity.status(401).body(res);

        }

        // Lấy email từ JWT principal
        String email = jwtUser.getEmail();

        // Lấy user từ DB để trả thông tin đầy đủ
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            res.setStatusCode(401);
            res.setMessage("Unauthorized");
            System.out.println("c");
            return ResponseEntity.status(401).body(res);
        }

        res.setStatusCode(200);
        res.setMessage("Authenticated");
        res.setUser(userMapper.toUserDTO(user));

        return ResponseEntity.ok(res);
    }



}

