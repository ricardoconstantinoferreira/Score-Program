package com.programa.pontos.dtos;

import com.programa.pontos.model.Score;

public record ResponseScoreDTO(String message, String code, Score newScore) {
}
