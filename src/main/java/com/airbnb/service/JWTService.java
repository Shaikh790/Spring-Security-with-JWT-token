package com.airbnb.service;

//import com.airbnb.entity.PropertyUser;
import com.airbnb.entity.PropertyUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class JWTService {
    @Value("${jwt.algorithm.key}") // Corrected property name
    private String algorithmKey;

    @Value("${jwt.issuer}") // Ensure issuer property is correctly configured in your application.properties/yml
    private String issuer;

    @Value("${jwt.expireDuration}")
    private int expirytime;

    private Algorithm algorithm;

    private final static String USER_NAME="username";

    @PostConstruct
    public void postConstruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateToken(PropertyUser propertyUser){
        return JWT.create()
                .withClaim(USER_NAME,propertyUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirytime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUsername(String token){
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
        return decodedJWT.getClaim(USER_NAME).asString();
    }
}