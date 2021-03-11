package com.epam.esm.contoller;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.util.jwt.JwtProvider;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Component;

public class UserRestRequestTests {

    private JwtProvider provider = new JwtProvider();
    @LocalServerPort
    private int serverPort;
    private String token;

    @BeforeEach
    void init() {
        RestAssured.port = serverPort;
        RestAssured.filters(new ResponseLoggingFilter());
        RestAssured.filters(new RequestLoggingFilter());
        RestAssured.baseURI = "http://localhost:" + serverPort + "/";
        UserDTO dto = UserDTO.builder().email("aaa").id(1).role(RoleDTO.builder().id(1).role("ROLE_USER").build()).build();
        token = provider.generateToken(dto);
        token = "Bearer " + token;
    }

    public String getToken() {
        return token;
    }
}
