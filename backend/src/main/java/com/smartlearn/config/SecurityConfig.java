package com.smartlearn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.smartlearn.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/health", "/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/courses/**")
                        .hasRole("INSTRUCTOR")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/courses/**")
                        .authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.POST,
                                "/api/courses/*/lessons")
                        .hasRole("INSTRUCTOR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                "/api/courses/*/lessons/*")
                        .hasRole("INSTRUCTOR")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                "/api/courses/*/lessons/*")
                        .hasRole("INSTRUCTOR")
                        .requestMatchers("/api/enrollments/**").hasRole("STUDENT")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
