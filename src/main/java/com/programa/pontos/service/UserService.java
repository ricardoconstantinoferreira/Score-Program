package com.programa.pontos.service;

import com.programa.pontos.dtos.UserDTO;
import com.programa.pontos.model.User;
import com.programa.pontos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<User> getAllUsers() throws Exception {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new Exception("Nobody Users");
        }

        return users;
    }

    public Optional<User> getUserById(int id) throws Exception {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new Exception("User with id " + id + " not found");
        }

        return user;
    }
}
