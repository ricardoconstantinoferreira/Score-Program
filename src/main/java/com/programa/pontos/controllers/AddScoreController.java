package com.programa.pontos.controllers;

import com.programa.pontos.dtos.AddStoreServiceDTO;
import com.programa.pontos.dtos.ResponseUserDTO;
import com.programa.pontos.model.User;
import com.programa.pontos.service.AddScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AddScoreController {

    @Autowired
    private AddScoreService addScoreService;

    @PutMapping("/add-store")
    public ResponseEntity<ResponseUserDTO> addStore(
            @RequestBody AddStoreServiceDTO addStoreServiceDTO
    ) throws Exception {
        User user = addScoreService.setStoreService(addStoreServiceDTO);
        String message = "Score Added Sucessfully!!!";

        if (addScoreService.validationScore(user)) {
            message = "Congratulations, Score Validated Successfully!!!";
        }

        Optional<User> optionalUser = Optional.ofNullable(user);
        ResponseUserDTO responseDTO = new ResponseUserDTO(message, "200", optionalUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);

    }

}
