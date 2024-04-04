package com.airbnb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;



@Configuration
public class SecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public SecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    //create security filetrchain method
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //cd2
        http.csrf().disable() // Disable CSRF protection
                .cors().disable();
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);  //add this class
        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/users/addUsers", "/api/v1/users/login")
                .permitAll()
                .requestMatchers("/api/v1/countries/addCountry").hasRole("ADMIN")
                .requestMatchers("/api/v1/users/profile").hasAnyRole("USER","ADMIN")
                .anyRequest().authenticated();

        return http.build();

    }

}
 