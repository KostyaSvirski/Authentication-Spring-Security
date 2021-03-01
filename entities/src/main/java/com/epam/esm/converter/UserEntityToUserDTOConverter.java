package com.epam.esm.converter;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.persistence.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserEntityToUserDTOConverter implements Function<UserEntity, UserDTO> {

    @Autowired
    private RoleEntityToRoleDTOConverter roleEntityToRoleDTOConverter;

    @Override
    public UserDTO apply(UserEntity user) {
        return UserDTO.builder().name(user.getName()).surname(user.getSurname())
                .email(user.getEmail()).password(user.getPassword())
                .role(roleEntityToRoleDTOConverter.apply(user.getRole()))
                .id(user.getId()).build();
    }
}
