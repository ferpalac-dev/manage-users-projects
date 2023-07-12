package com.management.users.repositories;

import com.management.users.entities.Project;
import com.management.users.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProjectRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    ProjectRepository projectRepository;

    @Test
    public void shouldNotFindProjects() {
        assertThat(projectRepository.findAll()).isEmpty();
    }

    @Test
    public void shouldCreateProject() {
        var project = new Project();
        project.setName("prj");
        project.setDescription("prj details");

        var user = new User();
        user.setName("testuser");
        user.setEmail("test@user.com");
        project.setUsers(List.of(user));

        var projectCreated = projectRepository.save(project);

        assertThat(projectCreated).isEqualTo(project);
    }

    @Test
    public void shouldGetProjectById() {
        var project = new Project();
        project.setName("prj");
        project.setDescription("prj details");
        project.setUsers(List.of());
        var projectCreated = entityManager.persist(project);

        var projectFound = projectRepository.findById(projectCreated.getId()).get();
        assertThat(projectFound.getName()).isEqualTo("prj");
        assertThat(projectFound.getDescription()).isEqualTo("prj details");
        assertThat(projectFound.getUsers()).isEmpty();
    }

    @Test
    public void shouldSearchUserByNameAndEmail() {
        var project1 = new Project();
        project1.setName("prj1");
        project1.setDescription("prj1 details");
        var projectCreated1 = entityManager.persist(project1);

        var project2 = new Project();
        project2.setName("prj2");
        project2.setDescription("prj2 details");
        entityManager.persist(project2);

        var projectsFound = projectRepository.findByNameIgnoreCaseContaining(
                "prj1", Pageable.ofSize(5));
        assertThat(projectsFound).hasSize(1).contains(projectCreated1);
    }

    @Test
    public void shouldDeleteUser() {
        var project = new Project();
        project.setName("prj");
        project.setDescription("prj details");
        var projectCreated = entityManager.persist(project);

        var projectsFound = projectRepository.findAll();
        assertThat(projectsFound).hasSize(1).contains(projectCreated);

        projectRepository.delete(projectCreated);
        assertThat(projectRepository.findAll()).isEmpty();
    }
}
