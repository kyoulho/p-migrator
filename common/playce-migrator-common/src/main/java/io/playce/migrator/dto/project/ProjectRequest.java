package io.playce.migrator.dto.project;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProjectRequest {
    @NotEmpty
    private String projectName;

    private String description;
    @NotNull
    private Long targetId;
}
