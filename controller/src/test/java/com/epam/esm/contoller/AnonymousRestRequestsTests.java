package com.epam.esm.contoller;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.web.server.LocalServerPort;

public class AnonymousRestRequestsTests {

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void init() {
        RestAssured.port = serverPort;
        RestAssured.filters(new ResponseLoggingFilter());
        RestAssured.filters(new RequestLoggingFilter());
        RestAssured.baseURI = "http://localhost:" + serverPort + "/";
    }
}
