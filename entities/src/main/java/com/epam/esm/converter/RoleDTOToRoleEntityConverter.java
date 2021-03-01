package com.epam.esm.converter;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.persistence.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RoleDTOToRoleEntityConverter implements Function<RoleDTO, RoleEntity> {

    @Autowired
    private UserDTOToUserEntityConverter userDTOToUserEntityConverter;

    @Override
    public RoleEntity apply(RoleDTO roleDTO) {
        return RoleEntity.builder().role(roleDTO.getRole()).build();
    }
}
