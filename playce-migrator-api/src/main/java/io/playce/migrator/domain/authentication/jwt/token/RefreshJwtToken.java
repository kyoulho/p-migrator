package io.playce.migrator.domain.authentication.jwt.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.domain.authentication.jwt.dto.Roles;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class RefreshJwtToken implements JwtToken {

    private String rawToken;

    @Getter
    private Claims claims;

    @Getter
    private Jws<Claims> jwsClaims;

    private RefreshJwtToken(Jws<Claims> jwsClaims) {
        this.jwsClaims = jwsClaims;
    }

    public RefreshJwtToken(String rawToken, Claims claims) {
        this.rawToken = rawToken;
        this.claims = claims;
    }

    public static RefreshJwtToken create(RawAccessJwtToken token, String signingKey) {
        Jws<Claims> claims = token.parseClaims(signingKey);

        List<String> roles = claims.getBody().get("roles", List.class);

        if(!roles.contains(Roles.REFRESH_TOKEN.authority())) {
            throw new PlayceMigratorException(ErrorCode.PM512A);
        }

        return new RefreshJwtToken(claims);
    }

    @Override
    public String getToken() {
        return this.rawToken;
    }
}
