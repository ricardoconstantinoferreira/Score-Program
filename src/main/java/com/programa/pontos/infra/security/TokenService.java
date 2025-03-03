package com.programa.pontos.infra.security;

import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired
    private JwtEncoder encoder;

    public String generateToken(Authentication authentication) {
        try {

            Instant now = Instant.now();
            long expiry = 3600L;

            String scopes = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            var claims = JwtClaimsSet.builder()
                    .issuer("jwt_pontos")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(authentication.getName())
                    .claim("scope", scopes)
                    .build();

            return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error in the generating token", e);
        }
    }
}
