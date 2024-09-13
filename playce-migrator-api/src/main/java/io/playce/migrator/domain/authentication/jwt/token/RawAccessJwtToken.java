package io.playce.migrator.domain.authentication.jwt.token;

import io.jsonwebtoken.*;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RawAccessJwtToken implements JwtToken {

    private final String token;

    @Override
    public String getToken() {
        return this.token;
    }

    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (SignatureException ex) {
            throw new PlayceMigratorException(ErrorCode.PM506A, ex);
        } catch (MalformedJwtException ex) {
            throw new PlayceMigratorException(ErrorCode.PM507A, ex);
        } catch (ExpiredJwtException ex) {
            throw new PlayceMigratorException(ErrorCode.PM508A, ex);
        } catch (UnsupportedJwtException ex) {
            throw new PlayceMigratorException(ErrorCode.PM509A, ex);
        } catch (IllegalArgumentException ex) {
            throw new PlayceMigratorException(ErrorCode.PM510A, ex);
        }
    }
}
