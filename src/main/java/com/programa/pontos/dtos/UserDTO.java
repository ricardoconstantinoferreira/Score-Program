package com.programa.pontos.dtos;

import jakarta.validation.constraints.NotNull;

public record UserDTO(@NotNull String name, @NotNull String document, @NotNull String email, @NotNull Float score) {
}
