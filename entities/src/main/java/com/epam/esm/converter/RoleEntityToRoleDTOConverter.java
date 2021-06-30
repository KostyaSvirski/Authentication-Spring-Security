package com.epam.esm.converter;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.persistence.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RoleEntityToRoleDTOConverter implements Function<RoleEntity, RoleDTO> {

    @Autowired
    UserEntityToUserDTOConverter userEntityToUserDTOConverter;

    @Override
    public RoleDTO apply(RoleEntity roleEntity) {
        return RoleDTO.builder().role(roleEntity.getRole()).id(roleEntity.getId()).build();
    }
}
