package com.programa.pontos.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.programa.pontos.model.UserAuthenticated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Authentication auth) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            var username = ((UserAuthenticated) ((UsernamePasswordAuthenticationToken) auth).getPrincipal()).getUsername();
            String token = JWT.create().withIssuer("jwt_pontos").withSubject(username)
                    .withExpiresAt(genExpirationDate()).sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error in the generating token", e);
        }
    }

    public String validateToken(String token) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("jwt_pontos")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
