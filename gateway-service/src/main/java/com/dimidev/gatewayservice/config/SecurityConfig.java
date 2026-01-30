package com.dimidev.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    private static final String[] FREE_RESOURCE_PATTERNS = {
            "/v3/api-docs/**",
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/aggregate/**",
            "/api/**"  // для тестирования через Swagger без аутентификации (dev)
    };

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher publicPaths = new OrRequestMatcher(
                Arrays.stream(FREE_RESOURCE_PATTERNS)
                        .map(AntPathRequestMatcher::antMatcher)
                        .collect(Collectors.toList())
        );
        http
                .securityMatcher(publicPaths)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public SecurityFilterChain securedSecurityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher publicPaths = new OrRequestMatcher(
                Arrays.stream(FREE_RESOURCE_PATTERNS)
                        .map(AntPathRequestMatcher::antMatcher)
                        .collect(Collectors.toList())
        );
        http
                .securityMatcher(new NegatedRequestMatcher(publicPaths))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                );
        return http.build();
    }
}
