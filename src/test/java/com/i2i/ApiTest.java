package com.i2i;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    @Order(1)
    @DisplayName("GET - List Posts")
    void testGetPosts() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", equalTo(100))
                .time(lessThan(3000L));

        System.out.println("SUCCESS: GET /posts returned 200 with 100 posts.");
    }

    @Test
    @Order(2)
    @DisplayName("GET - Single Post")
    void testGetSinglePost() {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .get("/posts/1")
                        .then()
                        .statusCode(200)
                        .body("id", equalTo(1))
                        .body("title", notNullValue())
                        .body("userId", notNullValue())
                        .extract().response();

        System.out.println("SUCCESS: Post title: " + response.jsonPath().getString("title"));
    }

    @Test
    @Order(3)
    @DisplayName("POST - Create Post")
    void testCreatePost() {
        String requestBody = "{ \"title\": \"Test Automation\", \"body\": \"i2i Academy\", \"userId\": 1 }";

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post("/posts")
                        .then()
                        .statusCode(201)
                        .body("title", equalTo("Test Automation"))
                        .body("id", notNullValue())
                        .time(lessThan(3000L))
                        .extract().response();

        System.out.println("SUCCESS: Created post ID: " + response.jsonPath().getString("id"));
    }

    @Test
    @Order(4)
    @DisplayName("GET - List Comments for Post")
    void testGetComments() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/posts/1/comments")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].email", containsString("@"))
                .time(lessThan(3000L));

        System.out.println("SUCCESS: GET /posts/1/comments returned valid comments.");
    }
}