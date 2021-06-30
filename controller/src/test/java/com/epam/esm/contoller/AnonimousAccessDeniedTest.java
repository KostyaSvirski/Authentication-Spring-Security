package com.epam.esm.contoller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnonimousAccessDeniedTest extends AnonymousRestRequestsTests {

    @Test
    void testAnonymousCertificatesPost() {
        given().baseUri(RestAssured.baseURI)
                .when().post("/certificates/")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testAnonymousCertificatesPatch() {
        given().baseUri(RestAssured.baseURI)
                .when().patch("/certificates/1")
                .then().assertThat().statusCode(403);
    }

    @Test
    void testAnonymousCertificatesDelete() {
        given().baseUri(RestAssured.baseURI)
                .when().delete("/certificates/1")
                .then().assertThat().statusCode(403);
    }

    @Test
    void testAnonymousTagsPost() {
        given().baseUri(RestAssured.baseURI)
                .when().post("/tags/")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testAnonymousTagsDelete() {
        given().baseUri(RestAssured.baseURI)
                .when().delete("/tags/1")
                .then().assertThat().statusCode(403);
    }

    @Test
    void testAnonymousUsersGet() {
        given().baseUri(RestAssured.baseURI)
                .when().get("/users/")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testAnonymousUserGet() {
        given().baseUri(RestAssured.baseURI)
                .when().get("/users/1")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testAnonymousOrdersGet() {
        given().baseUri(RestAssured.baseURI)
                .when().get("/orders/")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testAnonymousOrderGet() {
        given().baseUri(RestAssured.baseURI)
                .when().get("/orders/1")
                .then().assertThat().statusCode(401);
    }

    @Test
    void testAnonymousOrderPost() {
        given().baseUri(RestAssured.baseURI)
                .when().post("/orders/")
                .then().assertThat().statusCode(401);
    }
}