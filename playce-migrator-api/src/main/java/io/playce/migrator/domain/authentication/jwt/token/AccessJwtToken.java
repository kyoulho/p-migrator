package io.playce.migrator.domain.authentication.jwt.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.domain.authentication.jwt.dto.Roles;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

public class AccessJwtToken implements JwtToken {

    private String rawToken;

    @Getter
    private Claims claims;
    @Getter
    private Jws<Claims> jwsClaims;

    public AccessJwtToken(Jws<Claims> jwsClaims) { this.jwsClaims = jwsClaims; }

    public AccessJwtToken(String rawToken, Claims claims) {
        this.rawToken = rawToken;
        this.claims = claims;
    }

    public static Optional<AccessJwtToken> verify(RawAccessJwtToken token, String signingKey) {
        Jws<Claims> claims = token.parseClaims(signingKey);
        List<String> roles = claims.getBody().get("roles", List.class);

        if(roles.contains(Roles.REFRESH_TOKEN.authority())) {
            throw new PlayceMigratorException(ErrorCode.PM513A);
        }

        return Optional.of(new AccessJwtToken(claims));
    }

    @Override
    public String getToken() {
        return this.rawToken;
    }
}
