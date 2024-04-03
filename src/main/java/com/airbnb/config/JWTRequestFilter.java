package com.airbnb.config;



import com.airbnb.entity.PropertyUser;

import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
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
        if (tokenHeader!= null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            //get username from service layer
            String username = jwtService.getUsername(token);
            Optional<PropertyUser> opUser = userRepository.findByUsername(username);
            if(opUser.isPresent()) {
                PropertyUser propertyUser = opUser.get();
                //this is for server to undarstand who is the current user
                UsernamePasswordAuthenticationToken authentication=
                        new UsernamePasswordAuthenticationToken(propertyUser,null,new ArrayList<>());
                //adani sets new word biggest port

                authentication.setDetails(new WebAuthenticationDetails(request));
                //it set the session to current user login
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
    }
}
