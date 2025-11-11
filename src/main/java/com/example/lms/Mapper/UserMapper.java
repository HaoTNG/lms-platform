package com.example.lms.Mapper;
import com.example.lms.dto.UserDTO;
import com.example.lms.model.User;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
}
