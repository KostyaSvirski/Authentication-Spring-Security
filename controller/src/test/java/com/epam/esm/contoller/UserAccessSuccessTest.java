package com.epam.esm.contoller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserAccessSuccessTest extends UserRestRequestTests {

    @Test
    void accessToReadOrders() {
        given().baseUri(RestAssured.baseURI).header("Authorization", getToken())
                .when().get("orders/")
                .then().assertThat().statusCode(200);
    }

    @Test
    void accessToReadOrder() {
        given().baseUri(RestAssured.baseURI).header("Authorization", getToken())
                .when().get("orders/1")
                .then().assertThat().statusCode(200);
    }

    @Test
    void accessToReadUser() {
        given().baseUri(RestAssured.baseURI).header("Authorization", getToken())
                .when().get("users/me")
                .then().assertThat().statusCode(200);
    }




}