package com.programa.pontos.controllers;

import com.programa.pontos.dtos.ResponseUserDTO;
import com.programa.pontos.dtos.ResponseUsersDTO;
import com.programa.pontos.dtos.UserDTO;
import com.programa.pontos.model.User;
import com.programa.pontos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody UserDTO userDTO) {
        User user = userService.createUser(userDTO);
        Optional<User> optionalUser = Optional.ofNullable(user);
        ResponseUserDTO responseDTO = new ResponseUserDTO("User Saved Successfully", "200", optionalUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseUsersDTO> getAllUsers() throws Exception {
        List<User> users = userService.getAllUsers();
        ResponseUsersDTO responseDTO = new ResponseUsersDTO("Successfully Get All Users", "200", users);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable(value = "id") int id) throws Exception {
        Optional<User> user = userService.getUserById(id);
        ResponseUserDTO responseDTO = new ResponseUserDTO("Successfully Get User by Id", "200", user);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/user/document/{document}")
    public ResponseEntity<ResponseUserDTO> getUserByDocument(@PathVariable(value = "document") String document) throws Exception {
        Optional<User> user = userService.findUserByDocument(document);
        ResponseUserDTO responseDTO = new ResponseUserDTO("Successfully Get User by Document", "200", user);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
 }
