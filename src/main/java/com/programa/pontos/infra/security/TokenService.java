package com.programa.pontos.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.programa.pontos.model.User;
import com.programa.pontos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private UserService userService;

    public String generateToken(User user) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            var username = user.getUsername();

            String token = JWT.create()
                    .withIssuer("jwt_pontos")
                    .withSubject(username)
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);

            if (!token.isEmpty()) {
                userService.saveToken(user, token);
            }

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error in the generating token", e);
        }
    }

    public String validateToken(String token) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            var username = JWT.require(algorithm)
                    .withIssuer("jwt_pontos")
                    .build()
                    .verify(token).getSubject();

            if (!username.isEmpty()) {
                User user = userService.getUserByUsername(username);

                if (!user.getToken().equals(token)) {
                    return "";
                }
            }

            return username;


        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
