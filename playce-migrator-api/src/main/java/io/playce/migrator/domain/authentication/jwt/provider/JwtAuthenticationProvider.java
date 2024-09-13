package io.playce.migrator.domain.authentication.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.playce.migrator.config.JwtProperties;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.domain.authentication.jwt.token.JwtAuthenticationToken;
import io.playce.migrator.domain.authentication.jwt.token.RawAccessJwtToken;
import io.playce.migrator.dto.authentication.SecurityUser;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtProperties jwtProperties;
    private final ModelMapper modelMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessJwtToken = (RawAccessJwtToken) authentication.getCredentials();
        Jws<Claims> jwsClaims = rawAccessJwtToken.parseClaims(jwtProperties.getTokenSigningKey());
        SecurityUser securityUser;

        try {
            securityUser = modelMapper.map(jwsClaims.getBody().get("user"), SecurityUser.class);
        } catch (IllegalArgumentException ex) {
            throw new PlayceMigratorException(ErrorCode.PM510A);
        }

        List<String> roles = jwsClaims.getBody().get("roles", List.class);
        List<GrantedAuthority> userRoleAuthorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new JwtAuthenticationToken(securityUser, userRoleAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
