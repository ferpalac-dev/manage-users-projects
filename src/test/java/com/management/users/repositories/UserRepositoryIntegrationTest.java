package com.management.users.repositories;

import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.delete;

import com.management.users.UsersApplication;
import com.management.users.entities.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = UsersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserRepositoryIntegrationTest {
    private static final String USER_ENDPOINT = "http://localhost:8080/users";

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setName("testuser");
        user.setEmail(UUID.randomUUID() + "test@user.com");
        user.setProjects(List.of());

        user = userRepository.save(user);
    }

    @Test
    public void shouldGetUserOK() {
        get(USER_ENDPOINT + "/" + user.getId())
                .then().statusCode(HttpStatus.OK.value())
                .body("name", equalTo(user.getName()),
                        "email", equalTo(user.getEmail()));
    }

    @Test
    public void shouldNotGetUser() {
        get(USER_ENDPOINT + "/" + user.getId())
                .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldNewUserBeCreated() {
        var newUser = new User();
        newUser.setName("testnewuser");
        newUser.setEmail("testnewuser@user.com");
        newUser.setProjects(List.of());
        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when().post(USER_ENDPOINT)
                .then().statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(newUser.getName()),
                        "email", equalTo(newUser.getEmail()));
    }

    @Test
    public void shouldDeleteUserOK() {
        delete(USER_ENDPOINT + "/" + user.getId())
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldNotFoundUserToBeDeleted() {
        delete(USER_ENDPOINT + "/9999999")
                .then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
