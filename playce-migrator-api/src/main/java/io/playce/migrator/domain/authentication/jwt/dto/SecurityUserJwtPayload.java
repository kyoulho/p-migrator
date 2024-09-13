package io.playce.migrator.domain.authentication.jwt.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SecurityUserJwtPayload {
    private String loginId;
    private String username;

    @JsonIgnore
    private List<String> authorities;
}
