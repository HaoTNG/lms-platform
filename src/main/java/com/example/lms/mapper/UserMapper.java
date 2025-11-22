package com.example.lms.mapper;
import com.example.lms.dto.UserDTO;
import com.example.lms.model.User;
import com.example.lms.model.Admin;
import com.example.lms.model.Tutor;
import com.example.lms.model.Mentee;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        
        String role = determineRole(user);
        
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(role)
                .build();
    }

    public User toUser(UserDTO userDTO) {
        // Note: Cannot instantiate abstract User directly
        // Should use UserFactory instead for creating specific subclass
        if (userDTO == null) {
            return null;
        }
        
        // This method primarily for updating existing users
        // For creation, use UserFactory.createUser(role)
        return null;
    }
    
    private String determineRole(User user) {
        if (user instanceof Admin) {
            return "ADMIN";
        } else if (user instanceof Tutor) {
            return "TUTOR";
        } else if (user instanceof Mentee) {
            return "MENTEE";
        }
        return "MENTEE"; // Default
    }
}
