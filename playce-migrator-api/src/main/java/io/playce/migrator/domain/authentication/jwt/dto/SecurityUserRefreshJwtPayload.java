package io.playce.migrator.domain.authentication.jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUserRefreshJwtPayload {

    private String loginId;
    private String username;
}

