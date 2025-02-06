package com.programa.pontos.dtos;

import com.programa.pontos.model.User;

import java.util.List;

public record ResponseUsersDTO(String message, String code, List<User> users) {
}
