package com.example.lms.mapper;

import com.example.lms.dto.UserDTO;
import com.example.lms.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Arch Linux)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.userId( user.getUserId() );
        userDTO.name( user.getName() );
        userDTO.email( user.getEmail() );
        userDTO.role( user.getRole() );

        return userDTO.build();
    }

    @Override
    public User toUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUserId( userDTO.getUserId() );
        user.setName( userDTO.getName() );
        user.setRole( userDTO.getRole() );
        user.setEmail( userDTO.getEmail() );

        return user;
    }
}
