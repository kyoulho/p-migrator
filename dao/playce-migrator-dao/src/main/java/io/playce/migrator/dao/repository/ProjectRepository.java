package io.playce.migrator.dao.repository;

import io.playce.migrator.dao.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
