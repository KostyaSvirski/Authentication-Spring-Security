package com.epam.esm.util.jwt;

import com.epam.esm.config.SecurityConfig;
import com.epam.esm.dto.RoleDTO;
import com.epam.esm.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SecurityConfig.class}, loader = AnnotationConfigContextLoader.class)
class JwtProviderTest {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtProvider provider;

    @Test
    void generateToken() {
        UserDTO user = UserDTO.builder().name("stepan").id(1).email("aaa@gmail.com")
                .password(encoder.encode("aaa")).role(RoleDTO.builder().role("ROLE_USER").build())
                .surname("step").build();
        String token = provider.generateToken(user);
        assertNotNull(token);
    }

    @Test
    void getLoginFromToken() {
        UserDTO user = UserDTO.builder().name("stepan").id(1).email("aaa@gmail.com")
                .password(encoder.encode("aaa")).role(RoleDTO.builder().id(1).role("ROLE_USER").build())
                .surname("step").build();
        String token = provider.generateToken(user);
        String login = provider.getLoginFromToken(token);
        String commaSeparatedRoles = provider.getAuthoritiesFromToken(token);
        assertEquals(user.getEmail(), login);
    }

}