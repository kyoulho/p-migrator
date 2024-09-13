package io.playce.migrator.dao.mapper;

import io.playce.migrator.dto.project.ProjectResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProjectMapper {
    Optional<ProjectResponse> selectProjectWithTargetWithAppCount(Long projectId);

    List<ProjectResponse> selectProjectsWithTargetWithAppCount();
}
