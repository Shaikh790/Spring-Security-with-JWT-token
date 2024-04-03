package com.airbnb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    //create security filetrchain method
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        //cd2
        http.csrf().disable() // Disable CSRF protection
                .cors().disable();
        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/users/addUsers","/api/v1/users/login","/api/v1/users/hello")
                .permitAll()
                .anyRequest().authenticated();

        return  http.build();

    }

}
