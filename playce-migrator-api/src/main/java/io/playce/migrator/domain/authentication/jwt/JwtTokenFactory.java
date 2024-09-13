package io.playce.migrator.domain.authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.playce.migrator.config.JwtProperties;
import io.playce.migrator.domain.authentication.jwt.dto.Roles;
import io.playce.migrator.domain.authentication.jwt.dto.SecurityUserJwtPayload;
import io.playce.migrator.domain.authentication.jwt.token.AccessJwtToken;
import io.playce.migrator.domain.authentication.jwt.token.JwtToken;
import io.playce.migrator.domain.authentication.jwt.token.RefreshJwtToken;
import io.playce.migrator.dto.authentication.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static io.playce.migrator.constant.CommonConstants.JWT_ISSUER;

@Component
@RequiredArgsConstructor
public class JwtTokenFactory {

    private final JwtProperties jwtProperties;
    private final ModelMapper modelMapper;

    public JwtToken createAccessJwtToken(SecurityUser securityUser) {
        SecurityUserJwtPayload securityUserJwtPayload = modelMapper.map(securityUser, SecurityUserJwtPayload.class);
        securityUserJwtPayload.setUsername(securityUser.getUsername());

        Claims claims = Jwts.claims().setSubject("Playce-Migrator User Info.");
        claims.setIssuer(JWT_ISSUER);
        claims.put("user", securityUserJwtPayload);
//        String loginId = securityUser.getUsername();
//        claims.put("roles", userMapper.selectUserRoles(username));
        claims.put("roles", List.of("ROLE_ADMIN"));

        return new AccessJwtToken(generateToken(claims, jwtProperties.getTokenExpirationTime()), claims);
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public JwtToken createRefreshJwtToken(SecurityUser securityUser) {
        SecurityUserJwtPayload securityUserJwtPayload = modelMapper.map(securityUser, SecurityUserJwtPayload.class);

        Claims claims = Jwts.claims().setSubject("Playce-Migrator User Info.");
        claims.setIssuer(JWT_ISSUER);
        claims.put("loginId", securityUserJwtPayload.getLoginId());
        claims.put("username", securityUserJwtPayload.getUsername());
        claims.put("roles", Arrays.asList(Roles.REFRESH_TOKEN.authority()));

        return new RefreshJwtToken(generateToken(claims, jwtProperties.getRefreshTokenExpirationTime()), claims);
    }

    /**
     * Token을 만든다.
     */
    private String generateToken(Claims claims, int expirationTime) {
        LocalDateTime currentTime = LocalDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes(expirationTime).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getTokenSigningKey())
                .compact();
    }
}
