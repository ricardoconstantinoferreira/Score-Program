package com.programa.pontos.dtos;

import jakarta.validation.constraints.NotNull;

public record ScoreDTO(@NotNull Float value) {
}
