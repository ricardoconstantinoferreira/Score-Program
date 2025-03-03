package com.programa.pontos.controllers;

import com.programa.pontos.dtos.AuthDTO;
import com.programa.pontos.infra.security.TokenService;
import com.programa.pontos.model.User;
import com.programa.pontos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/auth/login")
    public String login(@RequestBody AuthDTO authDTO) {

        var usernamePassword =
                new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());

        var auth = authenticationManager.authenticate(usernamePassword);

        return tokenService.generateToken(auth);
    }
}
