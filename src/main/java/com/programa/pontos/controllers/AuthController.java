package com.programa.pontos.controllers;

import com.programa.pontos.dtos.AuthDTO;
import com.programa.pontos.infra.security.TokenService;
import com.programa.pontos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/auth/login")
    public String login(@RequestBody AuthDTO authDTO) {

        User user = new User();

        user.setUsername(authDTO.username());
        user.setPassword(authDTO.password());

        var usernamePassword =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        var auth = authenticationManager.authenticate(usernamePassword);
        return tokenService.generateToken(auth);
    }
}
