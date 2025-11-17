package com.example.lms.controller;

import com.example.lms.dto.Response;
import com.example.lms.dto.UserDTO;
import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import com.example.lms.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Register - Đăng ký tài khoản mới
     */
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        Response res = new Response();
        try {
            // Kiểm tra email đã tồn tại
            if (userRepository.findAll().stream()
                    .anyMatch(u -> u.getEmail().equals(userDTO.getEmail()))) {
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

            // Set token vào cookie
            Cookie cookie = new Cookie("Authorization", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);  // Set true khi production với HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 24 hours
            response.addCookie(cookie);

            res.setStatusCode(200);
            res.setMessage("User registered successfully");
            res.setUser(userDTO);
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
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response) {
        Response res = new Response();
        try {
            // Tìm user bằng email
            User user = userRepository.findAll().stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                res.setStatusCode(401);
                res.setMessage("Invalid email or password");
                return ResponseEntity.status(401).body(res);
            }

            // Kiểm tra password
            if (!passwordEncoder.matches(password, user.getPassword_hashed())) {
                res.setStatusCode(401);
                res.setMessage("Invalid email or password");
                return ResponseEntity.status(401).body(res);
            }

            // Tạo JWT token
            String token = jwtUtil.generateToken(
                    user.getUserId().toString(),
                    user.getEmail(),
                    user.getRole()
            );

            // Set token vào cookie
            Cookie cookie = new Cookie("Authorization", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);  // Set true khi production với HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 24 hours
            response.addCookie(cookie);

            UserDTO userDTO = new UserDTO();
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole());

            res.setStatusCode(200);
            res.setMessage("Login successful");
            res.setUser(userDTO);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.setStatusCode(500);
            res.setMessage("Error during login: " + e.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    /**
     * Logout - Đăng xuất
     */
    @PostMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletResponse response) {
        Response res = new Response();
        try {
            // Xóa cookie Authorization
            Cookie cookie = new Cookie("Authorization", "");
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(0);  // Expire immediately
            response.addCookie(cookie);

            res.setStatusCode(200);
            res.setMessage("Logout successful");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.setStatusCode(500);
            res.setMessage("Error during logout: " + e.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    /**
     * Check authentication status
     */
    @GetMapping("/me")
    public ResponseEntity<Response> getCurrentUser() {
        Response res = new Response();
        try {
            // User info sẽ được lấy từ SecurityContext đã set bởi JwtAuthenticationFilter
            res.setStatusCode(200);
            res.setMessage("User is authenticated");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.setStatusCode(401);
            res.setMessage("Unauthorized: " + e.getMessage());
            return ResponseEntity.status(401).body(res);
        }
    }
}

