package com.epam.esm.converter;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.persistence.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserDTOToUserEntityConverter implements Function<UserDTO, UserEntity> {

    private PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private RoleDTOToRoleEntityConverter roleDTOToRoleEntityConverter;

    @Override
    public UserEntity apply(UserDTO userDTO) {
        return UserEntity.builder().name(userDTO.getName()).surname(userDTO.getSurname())
                .email(userDTO.getEmail()).password(encoder.encode(userDTO.getPassword()))
                .role(roleDTOToRoleEntityConverter.apply(userDTO.getRole()))
                .build();
    }
}
