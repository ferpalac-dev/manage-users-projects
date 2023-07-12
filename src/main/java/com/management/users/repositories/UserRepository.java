package com.management.users.repositories;

import com.management.users.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    @RestResource(path = "byNameAndEmail", rel = "userSearch")
    List<User> findByNameAndEmailIgnoreCaseContaining(
            @Param("name") String name, @Param("email") String email, Pageable pageable);

}

