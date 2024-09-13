package io.playce.migrator.dao.repository;

import io.playce.migrator.dao.entity.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, String>, JpaSpecificationExecutor<UserAccess> {
}
