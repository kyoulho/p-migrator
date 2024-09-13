package io.playce.migrator.dto.project;

import io.playce.migrator.dto.target.TargetResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class ProjectResponse{
    private Long projectId;
    private String projectName;
    private String description;

    private TargetResponse target;

    private Long applicationCount;

    private Instant registDatetime;
    private String registLoginId;
    private Instant modifyDatetime;
    private String modifyLoginId;
}
