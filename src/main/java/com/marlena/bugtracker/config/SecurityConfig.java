package com.marlena.bugtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/my-profile/**").authenticated()
                        .requestMatchers("/users/new").hasRole("MANAGE_USER")
                        .requestMatchers("/users/authorities").hasRole("MANAGE_USER")
                        .requestMatchers("/users/edit/**").hasRole("MANAGE_USER")
                        .requestMatchers("/users/delete/**").hasRole("MANAGE_USER")
                        .requestMatchers("/users").hasRole("USER_TAB")
                        .requestMatchers("/projects/new").hasRole("MANAGE_PROJECT")
                        .requestMatchers("/projects/edit/**").hasRole("MANAGE_PROJECT")
                        .requestMatchers("/projects/delete/**").hasRole("MANAGE_PROJECT")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login").defaultSuccessUrl("/my-profile").failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout().logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).permitAll();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
