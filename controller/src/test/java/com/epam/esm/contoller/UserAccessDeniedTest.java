package com.epam.esm.contoller;

import com.epam.esm.config.SecurityConfig;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAccessDeniedTest extends UserRestRequestTests {

    @Test
    void testUserCertificatesPost() {
        given().baseUri(RestAssured.baseURI).header("Authorization", getToken())
                .when().post("/certificates/")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testUserTagsPost() {
        given().baseUri(RestAssured.baseURI).header("Authorization", getToken())
                .when().post("/tags/")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testUserUsersGet() {
        given().baseUri(RestAssured.baseURI).header("Authorization", getToken())
                .when().get("/users/")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testUserUserGet() {
        given().baseUri(RestAssured.baseURI).header("Authorization", getToken())
                .when().get("/users/1")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testUserOrdersGet() {
        given().baseUri(RestAssured.baseURI).header("Authorization", getToken())
                .when().get("/orders/")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testUserOrderGet() {
        given().baseUri(RestAssured.baseURI).header("Authorization", getToken())
                .when().get("/orders/1")
                .then().assertThat().statusCode(401);
    }

}
