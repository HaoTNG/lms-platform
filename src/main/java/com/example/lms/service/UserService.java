package com.example.lms.service;

import com.example.lms.dto.UserDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDTO createUser(UserDTO dto);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO dto);
    void deleteUser(Long id);
}
