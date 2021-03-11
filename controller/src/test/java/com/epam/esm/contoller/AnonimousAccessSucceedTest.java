package com.epam.esm.contoller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.auth.AuthRequest;
import com.epam.esm.service.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnonimousAccessSucceedTest extends AnonymousRestRequestsTests {

    @Mock
    private UserService service;
    @InjectMocks
    private AuthController controller;

    @Test
    void testAnonymousCertificatesGet() {
        given().baseUri(RestAssured.baseURI)
                .when().get("/certificates/")
                .then().assertThat().statusCode(200);
    }

    @Test
    void testAnonymousCertificateGet() {
        given().baseUri(RestAssured.baseURI)
                .when().get("/certificates/1")
                .then().assertThat().statusCode(200);
    }

    @Test
    void testAnonymousTagsGet() {
        given().baseUri(RestAssured.baseURI)
                .when().get("/tags/")
                .then().assertThat().statusCode(200);
    }

    @Test
    void testAnonymousTagGet() {
        given().baseUri(RestAssured.baseURI)
                .when().get("/tags/1")
                .then().assertThat().statusCode(200);
    }

    @Test
    void testAnonymousSignIn() {
        AuthRequest request = new AuthRequest();
        request.setUsername("aaa@email.com");
        request.setPassword("1Aa2345678");
        given().baseUri(RestAssured.baseURI).contentType(ContentType.JSON).body(request)
                .when().post("/signin")
                .then().assertThat().statusCode(200);
    }

    @Test
    void testAnonymousSignUp() {
        UserDTO dto = UserDTO.builder().surname("aaa").name("aaa").email("aaa@gmail.com").password("aaa").build();
        when(service.createUser(dto)).thenReturn(1L);
        given().baseUri(RestAssured.baseURI).contentType(ContentType.JSON).body(dto)
                .when().post("/signup")
                .then().assertThat().statusCode(200);
    }


}