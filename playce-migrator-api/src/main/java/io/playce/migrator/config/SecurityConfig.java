package io.playce.migrator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.domain.authentication.jwt.extractor.TokenExtractor;
import io.playce.migrator.domain.authentication.jwt.filter.ExceptionHandlerFilter;
import io.playce.migrator.domain.authentication.jwt.filter.JwtTokenAuthenticationProcessingFilter;
import io.playce.migrator.domain.authentication.jwt.filter.SkipPathRequestMatcher;
import io.playce.migrator.domain.authentication.jwt.provider.CustomAuthenticationProvider;
import io.playce.migrator.domain.authentication.jwt.provider.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String CONSOLE_URL = "/console/**";
    private static final String API_AUTH_URL = "/api/auth/**";
    private static final String API_ROOT_URL = "/api/**";

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final TokenExtractor tokenExtractor;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;


    /**
     * WebSecurityConfigurerAdapter 가 deprecated 됨에 따라 @Bean으로 등록해서 사용하는 방식 사용
     * <a href="https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html">...</a>
     * <a href="https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter">...</a>
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(CONSOLE_URL, API_AUTH_URL, "/api/common/codes");
        httpSecurity
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/websocket/**").permitAll()
                .and()
                .addFilterBefore(new ExceptionHandlerFilter(messageSource, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenAuthenticationProcessingFilter(tokenExtractor, new SkipPathRequestMatcher(permitAllEndpointList, API_ROOT_URL), jwtAuthenticationProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .formLogin().disable()
                .authenticationProvider(customAuthenticationProvider);
//                .authenticationProvider(jwtAuthenticationProvider);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow URL
        configuration.addAllowedOriginPattern("*");
        // Allow Header
        configuration.addAllowedHeader("*");
        // Allow Http Method
        configuration.addAllowedMethod("*");
        //configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.addExposedHeader("Content-Disposition");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
