package com.example.demo.config;

import com.example.demo.filter.JWTAuthenticationFilter;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTAuthenticationFilter authenticationFilter;
    private final UserService userService;

    @Autowired
    public SecurityConfig(JWTAuthenticationFilter authenticationFilter, @Lazy UserService userService) {
        this.authenticationFilter = authenticationFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/api/user/login", "/api/user/register", "/api/posts").permitAll()
                            .requestMatchers("/api/posts/**", "/api/user/**").authenticated()
                            .requestMatchers("/api/private/**").hasAnyAuthority("ADMIN", "MODERATOR")
                            .requestMatchers("/api/private/role/**").hasAuthority("ADMIN")
                            .anyRequest().authenticated();
                })
                .userDetailsService(userService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(@Lazy AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
