package com.programa.pontos.service;

import com.programa.pontos.dtos.UserDTO;
import com.programa.pontos.model.User;
import com.programa.pontos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserDTO userDTO) {
        User user = new User(
                0,
                userDTO.name(),
                userDTO.document(),
                userDTO.email(),
                userDTO.score()
        );

        return userRepository.save(user);
    }
}
