package io.playce.migrator.domain.authentication.controller;

import io.playce.migrator.config.JwtProperties;
import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.domain.authentication.dto.LoginRequest;
import io.playce.migrator.domain.authentication.dto.LoginResponse;
import io.playce.migrator.domain.authentication.jwt.JwtTokenFactory;
import io.playce.migrator.domain.authentication.jwt.extractor.JwtHeaderTokenExtractor;
import io.playce.migrator.domain.authentication.jwt.token.RawAccessJwtToken;
import io.playce.migrator.domain.authentication.jwt.token.RefreshJwtToken;
import io.playce.migrator.domain.authentication.service.CustomUserDetailsService;
import io.playce.migrator.dto.authentication.SecurityUser;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.GeneralCipherUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static io.playce.migrator.constant.CommonConstants.AUTHENTICATION_HEADER_NAME;
import static io.playce.migrator.util.StringNullChecker.isEmpty;

@Slf4j
@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtHeaderTokenExtractor jwtHeaderTokenExtractor;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenFactory jwtTokenFactory;
    private final JwtProperties jwtProperties;
    private final PlayceMigratorConfig playceMigratorConfig;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
//        log.debug(":+:+:+:+: username : {}, password : {}", loginRequest.getUsername(), loginRequest.getPassword());

        if (isEmpty(loginRequest.getUsername()) && isEmpty(loginRequest.getPassword())) {
            throw new PlayceMigratorException(ErrorCode.PM107H);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(GeneralCipherUtil.decrypt(loginRequest.getUsername()), GeneralCipherUtil.decrypt(loginRequest.getPassword())));

            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtTokenFactory.createAccessJwtToken(securityUser).getToken());
            loginResponse.setRefreshToken(jwtTokenFactory.createRefreshJwtToken(securityUser).getToken());
            return loginResponse;
        } catch (PlayceMigratorException e) {
            if (e.getErrorCode() == ErrorCode.PM515A) {
                throw new PlayceMigratorException(ErrorCode.PM517A);
            }
            throw e;
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/refresh-token")
    public LoginResponse refreshToken(HttpServletRequest request) {
        String tokenPayload = jwtHeaderTokenExtractor.extract(request.getHeader(AUTHENTICATION_HEADER_NAME));
        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);

        RefreshJwtToken refreshToken = RefreshJwtToken.create(rawToken, jwtProperties.getTokenSigningKey());

        String username = (String) refreshToken.getJwsClaims().getBody().get("loginId");
        SecurityUser securityUser = (SecurityUser) customUserDetailsService.loadUserByUsername(username);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtTokenFactory.createAccessJwtToken(securityUser).getToken());
        loginResponse.setRefreshToken(jwtTokenFactory.createRefreshJwtToken(securityUser).getToken());

        return loginResponse;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/version")
    public String getVersion() {
        return playceMigratorConfig.getVersion();
    }
}
