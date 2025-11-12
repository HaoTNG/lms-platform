package com.example.lms.service.interf;


import com.example.lms.dto.Response;
import com.example.lms.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface AdminService {
    Response ManageUser();
    Response GetUserById(String id);
    Response createUser(UserDTO user);
    Response getAllCourses();

}
