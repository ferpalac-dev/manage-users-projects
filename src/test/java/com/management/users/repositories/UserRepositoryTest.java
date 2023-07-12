package com.management.users.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.management.users.entities.Project;
import com.management.users.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldNotFindUsers() {
        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    public void shouldCreateUser() {
        var user = new User();
        user.setName("testuser");
        user.setEmail("test@user.com");
        user.setProjects(List.of());
        var userCreated = userRepository.save(user);

        assertThat(userCreated.getName()).isEqualTo("testuser");
        assertThat(userCreated.getEmail()).isEqualTo("test@user.com");
        assertThat(userCreated.getProjects()).isEmpty();
    }

    @Test
    public void shouldGetUserById() {
        var user = new User();
        user.setName("testuser");
        user.setEmail("test@user.com");

        var project = new Project();
        project.setName("RC");
        project.setDescription("rc description");
        user.setProjects(List.of(project));

        var userCreated = entityManager.persist(user);

        var userFound = userRepository.findById(user.getId()).get();
        assertThat(userFound.getName()).isEqualTo("testuser");
        assertThat(userCreated.getEmail()).isEqualTo("test@user.com");
        assertThat(userCreated.getProjects()).hasSize(1).contains(project);
    }

    @Test
    public void shouldSearchUserByNameAndEmail() {
        var user1 = new User();
        user1.setName("testuser1");
        user1.setEmail("test1@user.com");
        var userCreated1 = entityManager.persist(user1);

        var user2 = new User();
        user2.setName("testuser2");
        user2.setEmail("test2@user.com");
        entityManager.persist(user2);

        var usersFound = userRepository.findByNameAndEmailIgnoreCaseContaining(
                "testuser1", "test1@user.com", Pageable.ofSize(2));
        assertThat(usersFound).hasSize(1).contains(userCreated1);
    }

    @Test
    public void shouldDeleteUser() {
        var user = new User();
        user.setName("testuser");
        user.setEmail("test@user.com");
        var userCreated = userRepository.save(user);

        var users = userRepository.findAll();
        assertThat(users).hasSize(1).contains(userCreated);

        userRepository.delete(userCreated);
        assertThat(userRepository.findAll()).isEmpty();
    }
}
