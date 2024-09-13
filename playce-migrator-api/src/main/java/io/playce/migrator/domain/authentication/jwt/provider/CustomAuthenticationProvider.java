package io.playce.migrator.domain.authentication.jwt.provider;

import io.playce.migrator.domain.authentication.service.CustomUserDetailsService;
import io.playce.migrator.dto.authentication.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        SecurityUser securityUser = (SecurityUser) customUserDetailsService.loadUserByUsername(username);

//        if (securityUser.getBlockYn().equals(CommonConstants.NO)) {
        if (PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(password, securityUser.getPassword())) {
            // Insert Success Login History
            customUserDetailsService.successLogin(username);
        } else {
            // Insert Fail Login History
            customUserDetailsService.failLogin(username);
        }

        return new UsernamePasswordAuthenticationToken(securityUser, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
