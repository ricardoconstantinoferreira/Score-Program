package com.programa.pontos.dtos;

import com.programa.pontos.model.User;

import java.util.Optional;

public record ResponseUserDTO(String message, String code, Optional<User> user) {
}
