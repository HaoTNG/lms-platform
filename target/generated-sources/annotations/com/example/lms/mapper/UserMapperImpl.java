package com.example.lms.mapper;

import com.example.lms.dto.UserDTO;
import com.example.lms.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.email( user.getEmail() );
        userDTO.name( user.getName() );
        userDTO.password_hashed( user.getPassword_hashed() );
        userDTO.role( user.getRole() );
        userDTO.userId( user.getUserId() );

        return userDTO.build();
    }

    @Override
    public User toUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( userDTO.getEmail() );
        user.setName( userDTO.getName() );
        user.setPassword_hashed( userDTO.getPassword_hashed() );
        user.setRole( userDTO.getRole() );
        user.setUserId( userDTO.getUserId() );

        return user;
    }
}
