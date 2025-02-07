package com.programa.pontos.service;

import com.programa.pontos.dtos.AddStoreServiceDTO;
import com.programa.pontos.dtos.UserDTO;
import com.programa.pontos.model.Score;
import com.programa.pontos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddScoreService {

    @Autowired
    private ScoreService storeService;

    @Autowired
    private UserService userService;

    public User setStoreService(AddStoreServiceDTO addStoreServiceDTO) throws Exception {
        Optional<User> user =  userService.findUserByDocument(addStoreServiceDTO.document());

        if (user.isEmpty()) {
            throw new Exception("User not found.");
        }

        Float newStore = user.get().getScore() + addStoreServiceDTO.score();
        UserDTO userDTO = new UserDTO("", "", "", newStore);
        return userService.updateUser(user.get().getId(), userDTO);
    }

    public boolean validationScore(User user) throws Exception {
        Score score = storeService.getScore();
        Float value = score.getValue();

        if (user.getScore() >= value) {
            UserDTO userDTO = new UserDTO("", "", "", (float) 0);
            userService.updateUser(user.getId(), userDTO);
            return true;
        }

        return false;
    }
}
