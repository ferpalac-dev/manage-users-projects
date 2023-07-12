package com.management.users.repositories;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.management.users.UsersApplication;
import com.management.users.entities.Project;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;


@SpringBootTest(classes = UsersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProjectRepositoryIntegrationTest {
    private static final String PROJECT_ENDPOINT = "http://localhost:8080/projects";

    @Autowired
    private ProjectRepository projectRepository;

    private Project project;

    @BeforeEach
    public void setup() {
        project = new Project();
        project.setName("prj");
        project.setDescription("prj details");

        project = projectRepository.save(project);
    }

    @Test
    public void shouldGetProjectOK() {
        get(PROJECT_ENDPOINT + "/" + project.getId())
                .then().statusCode(HttpStatus.OK.value())
                .body("name", equalTo(project.getName()),
                        "description", equalTo(project.getDescription()));
    }

    @Test
    public void shouldNotGetProject() {
        get(PROJECT_ENDPOINT + "/999999")
                .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldNewProjectBeCreated() {
        var newProject = new Project();
        newProject.setName("new prj");
        newProject.setDescription("new project details");
        given()
                .contentType(ContentType.JSON)
                .body(newProject)
                .when().post(PROJECT_ENDPOINT)
                .then().statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(newProject.getName()),
                        "description", equalTo(newProject.getDescription()));
    }

    @Test
    public void shouldDeleteProjectOK() {
        delete(PROJECT_ENDPOINT + "/" + project.getId())
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldNotFoundProjectToBeDeleted() {
        delete(PROJECT_ENDPOINT + "/9999999")
                .then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
