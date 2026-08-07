package com.projectecho.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class SecurityConfig {

    public SecurityConfig() {
        super();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(
                                                "/actuator/health/**",
                                                "/actuator/health/readiness",
                                                "/actuator/health/liveness",
                                                "/actuator/**",
                                                "/v3/api-docs/**",
                                                "/swagger-ui/**",
                                                "/swagger-ui.html",
                                                "/api/v1/**")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated());

        return http.build();
    }
}
