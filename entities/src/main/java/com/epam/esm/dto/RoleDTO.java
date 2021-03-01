package com.epam.esm.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO extends RepresentationModel<RoleDTO> {

    private long id;
    private String role;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Autowired
    private Set<UserDTO> users;
}