package com.airbnb.config;



import com.airbnb.entity.PropertyUser;

import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private JWTService jwtService;

    private PropertyUserRepository userRepository;

    public JWTRequestFilter(JWTService jwtService, PropertyUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(8,tokenHeader.length()-1);
            // Get username from service layer
            String username = jwtService.getUsername(token);
            Optional<PropertyUser> opUser = userRepository.findByUsername(username);
            if (opUser.isPresent()) {
                PropertyUser propertyUser = opUser.get();
                // Set authentication details
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(propertyUser, null, Collections.singletonList(new SimpleGrantedAuthority(propertyUser.getUserRole())));//update here for ROLE BAsed
                authentication.setDetails(new WebAuthenticationDetails(request));
                // Set the session to current user login
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // Continue with the filter chain
         filterChain.doFilter(request, response);
    }
}
