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
    @Value("${jwt.algorithms.key}")
    private String algorithmKey;
    @Value("${jwt.issuer")
    private String issuer;
    @Value("${jwt.expireDuration}")
    private int expirytime;

    private Algorithm algorithm;

    private final static String USER_NAME="username";
    //it give algorithm
    @PostConstruct
    public void postConstruct(){
//        System.out.println(algorithmKey);
//        System.out.println(issuer);
//        System.out.println(expirytime);
         algorithm = Algorithm.HMAC256(algorithmKey);


    }
    public String generateToken(PropertyUser user){
     return JWT.create()
                .withClaim("USER_NAME",user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+expirytime))
                .withIssuer(issuer)
                .sign(algorithm);

    }
    //verify the token and return username if valid
    public String getUsername(String token){
        //rwbv(rosy with pony v)
        DecodedJWT decodedJWT=JWT.require(algorithm)
                .withIssuer(issuer)
               .build()
               .verify(token);
        //extract username
      return  decodedJWT.getClaim(USER_NAME).asString();

    }

}
