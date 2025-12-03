package com.example.lms.service.imple;


import com.example.lms.security.JwtUserDetails;
import com.example.lms.service.interf.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImple implements UserService {


    @Override
    public Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
            return Long.parseLong(userDetails.getUserId());
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }
}
