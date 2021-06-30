package com.epam.esm.converter;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.persistence.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RoleDTOToRoleEntityConverter implements Function<RoleDTO, RoleEntity> {

    @Override
    public RoleEntity apply(RoleDTO roleDTO) {
        return RoleEntity.builder().id(roleDTO.getId()).role(roleDTO.getRole()).build();
    }
}
