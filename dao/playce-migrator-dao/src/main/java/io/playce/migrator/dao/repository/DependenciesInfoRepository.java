package io.playce.migrator.dao.repository;

import io.playce.migrator.dao.entity.DependenciesInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependenciesInfoRepository extends JpaRepository<DependenciesInfo, Long> {
    List<DependenciesInfo> findByApplicationId(Long applicationId);

    void deleteByApplicationId(Long applicationId);
}
