package com.epam.esm.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO implements GrantedAuthority {

    private long id;
    private String role;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<UserDTO> users;

    @Override
    public String getAuthority() {
        return role;
    }
}
