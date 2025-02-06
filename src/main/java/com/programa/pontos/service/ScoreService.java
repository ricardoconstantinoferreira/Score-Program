package com.programa.pontos.service;

import com.programa.pontos.dtos.ScoreDTO;
import com.programa.pontos.model.Score;
import com.programa.pontos.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    public Score createScore(ScoreDTO scoreDTO) throws Exception {

        List<Score> scores = scoreRepository.findAll();

        if (!scores.isEmpty()) {
            throw new Exception("Not possible add more than one register.");
        }

        var score = new Score(0, scoreDTO.value());
        return scoreRepository.save(score);
    }

    public Score updateScore(int id, ScoreDTO scoreDTO) throws Exception {
        Optional<Score> score = scoreRepository.findById(id);

        if (score.isEmpty()) {
            throw new Exception("Score not found");
        }

        var scoreModel = score.get();
        scoreModel.setValue(scoreDTO.value());

        return scoreRepository.save(scoreModel);
    }

    public Score getScore() throws Exception {
        List<Score> scores = scoreRepository.findAll();

        if (scores.isEmpty()) {
            throw new Exception("Not found score, please add one.");
        }

        return scores.get(0);
    }
}
