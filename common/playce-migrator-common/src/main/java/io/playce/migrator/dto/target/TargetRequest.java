package io.playce.migrator.dto.target;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class TargetRequest{
    @NotEmpty
    private String osName;
    @NotEmpty
    private String middlewareName;

    private String middlewareVersion;
    @NotEmpty
    private String javaVersion;
    //@NotNull
    private Boolean containerizationYn;
}
