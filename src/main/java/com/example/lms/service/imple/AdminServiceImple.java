package com.example.lms.service.imple;

import com.example.lms.Mapper.UserMapper;
import com.example.lms.dto.Response;
import com.example.lms.repository.AdminRepository;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.interf.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminServiceImple implements AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Response ManageUser() {
        Response response = new Response();
        try{
            response.setUserList(userRepository.findAll().stream().map(userMapper::toUserDTO).toList());
            response.setMessage("Success");
            response.setStatusCode(200);
        }catch(Exception e){
            response.setMessage("Users retrieved failed");
            response.setStatusCode(400);
        }
        return response;
    }
}
