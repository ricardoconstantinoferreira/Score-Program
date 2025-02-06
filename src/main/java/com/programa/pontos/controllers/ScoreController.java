package com.programa.pontos.controllers;

import com.programa.pontos.dtos.ResponseScoreDTO;
import com.programa.pontos.dtos.ScoreDTO;
import com.programa.pontos.model.Score;
import com.programa.pontos.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping("/score")
    public ResponseEntity<ResponseScoreDTO> createScore(@RequestBody ScoreDTO scoreDTO) throws Exception {
        Score newScore = scoreService.createScore(scoreDTO);
        ResponseScoreDTO responseDTO = new ResponseScoreDTO("Successfully Add Score", "200", newScore);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PutMapping("/score/{id}")
    public ResponseEntity<ResponseScoreDTO> updateScore(
            @PathVariable(value = "id") int id,
            @RequestBody ScoreDTO scoreDTO
    ) throws Exception {
        Score newScore = scoreService.updateScore(id, scoreDTO);
        ResponseScoreDTO responseDTO = new ResponseScoreDTO("Successfully Updated Score", "200", newScore);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/score")
    public ResponseEntity<ResponseScoreDTO> getScore() throws Exception {
        Score newScore = scoreService.getScore();
        ResponseScoreDTO responseDTO = new ResponseScoreDTO("Successfully Get Score", "200", newScore);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
