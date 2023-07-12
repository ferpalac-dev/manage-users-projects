package com.management.users.repositories;

import com.management.users.entities.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Long>, PagingAndSortingRepository<Project, Long> {
    @RestResource(path = "byName", rel = "projectSearch")
    List<Project> findByNameIgnoreCaseContaining(@Param("name") String name, Pageable pageable);
}
