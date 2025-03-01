package com.programa.pontos.dtos;

import com.programa.pontos.model.User;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record ResponseUserDTO(String message, String code, @NotBlank Optional<User> user) {
}
