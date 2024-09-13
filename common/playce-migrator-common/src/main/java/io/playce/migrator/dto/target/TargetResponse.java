package io.playce.migrator.dto.target;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TargetResponse{
    private Long targetId;
    private String osName;
    private String middlewareName;
    private String middlewareVersion;
    private String javaVersion;
    private Boolean containerizationYn;
}
