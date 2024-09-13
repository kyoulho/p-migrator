package io.playce.migrator.domain.authentication.jwt.filter;

import io.playce.migrator.domain.authentication.jwt.extractor.TokenExtractor;
import io.playce.migrator.domain.authentication.jwt.provider.JwtAuthenticationProvider;
import io.playce.migrator.domain.authentication.jwt.token.JwtAuthenticationToken;
import io.playce.migrator.domain.authentication.jwt.token.RawAccessJwtToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.playce.migrator.constant.CommonConstants.AUTHENTICATION_HEADER_NAME;

public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenExtractor tokenExtractor;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public JwtTokenAuthenticationProcessingFilter(TokenExtractor tokenExtractor,
                                                  RequestMatcher matcher, JwtAuthenticationProvider jwtAuthenticationProvider) {
        super(matcher);
        this.tokenExtractor = tokenExtractor;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String tokenPayload = request.getHeader(AUTHENTICATION_HEADER_NAME);
        RawAccessJwtToken token = new RawAccessJwtToken(tokenExtractor.extract(tokenPayload));

        return jwtAuthenticationProvider.authenticate(new JwtAuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed){
        SecurityContextHolder.clearContext();
    }
}
