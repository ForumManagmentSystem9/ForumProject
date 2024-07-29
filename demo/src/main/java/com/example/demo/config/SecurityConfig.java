package com.example.demo.config;

import com.example.demo.filter.JWTAuthenticationFilter;
import com.example.demo.services.UserService;
import com.example.demo.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTAuthenticationFilter authenticationFilter;
    private final UserServiceImpl userService;

    @Autowired
    public SecurityConfig(JWTAuthenticationFilter authenticationFilter, UserServiceImpl userService) {
        this.authenticationFilter = authenticationFilter;
        this.userService = userService;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/login/**", "/register/**").permitAll()
                            .requestMatchers("/api/users/**").hasAuthority("ADMIN")
                            .anyRequest().authenticated();
                })
                .userDetailsService(userService).sessionManagement(session ->{
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}