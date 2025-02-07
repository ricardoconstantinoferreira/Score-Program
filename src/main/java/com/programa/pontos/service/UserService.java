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

    public User updateUser(int id, UserDTO userDTO) throws Exception {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new Exception("User not found.");
        }

        var userModel = user.get();

        if (!userDTO.name().isEmpty()) {
            userModel.setName(userDTO.name());
        }

        if (!userDTO.email().isEmpty()) {
            userModel.setEmail(userDTO.email());
        }

        if (!userDTO.document().isEmpty()) {
            userModel.setDocument(userDTO.document());
        }

        if (userDTO.score() != null) {
            userModel.setScore(userDTO.score());
        }

        return userRepository.save(userModel);
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
            throw new Exception("User with id " + id + " not found.");
        }

        return user;
    }

    public Optional<User> findUserByDocument(String document) throws Exception {
        Optional<User> user = userRepository.findUserByDocument(document);

        if (user.isEmpty()) {
            throw new Exception("User with document " + document + " not found.");
        }

        return user;
    }
}
