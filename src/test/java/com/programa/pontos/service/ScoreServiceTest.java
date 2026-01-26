package com.programa.pontos.service;

import com.programa.pontos.dtos.ScoreDTO;
import com.programa.pontos.model.Score;
import com.programa.pontos.repository.ScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScoreService Tests")
class ScoreServiceTest {

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private ScoreService scoreService;

    private Score score;
    private ScoreDTO scoreDTO;

    @BeforeEach
    void setUp() {
        score = new Score(1, 100f);
        scoreDTO = new ScoreDTO(100f);
    }

    @Test
    @DisplayName("Should create a score when no scores exist")
    void testCreateScoreSuccess() throws Exception {
        // Arrange
        when(scoreRepository.findAll()).thenReturn(new ArrayList<>());
        when(scoreRepository.save(any(Score.class))).thenReturn(score);

        // Act
        Score result = scoreService.createScore(scoreDTO);

        // Assert
        assertNotNull(result);
        assertEquals(100f, result.getValue());
        verify(scoreRepository, times(1)).findAll();
        verify(scoreRepository, times(1)).save(any(Score.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to create score and one already exists")
    void testCreateScoreFailure() {
        // Arrange
        List<Score> scores = new ArrayList<>();
        scores.add(score);
        when(scoreRepository.findAll()).thenReturn(scores);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            scoreService.createScore(scoreDTO);
        });

        assertEquals("Not possible add more than one register.", exception.getMessage());
        verify(scoreRepository, times(1)).findAll();
        verify(scoreRepository, never()).save(any(Score.class));
    }

    @Test
    @DisplayName("Should update score successfully")
    void testUpdateScoreSuccess() throws Exception {
        // Arrange
        ScoreDTO updateDTO = new ScoreDTO(150f);
        when(scoreRepository.findById(1)).thenReturn(Optional.of(score));
        when(scoreRepository.save(any(Score.class))).thenReturn(score);

        // Act
        Score result = scoreService.updateScore(1, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(150f, result.getValue());
        verify(scoreRepository, times(1)).findById(1);
        verify(scoreRepository, times(1)).save(any(Score.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to update non-existent score")
    void testUpdateScoreNotFound() {
        // Arrange
        ScoreDTO updateDTO = new ScoreDTO(150f);
        when(scoreRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            scoreService.updateScore(999, updateDTO);
        });

        assertEquals("Score not found", exception.getMessage());
        verify(scoreRepository, times(1)).findById(999);
        verify(scoreRepository, never()).save(any(Score.class));
    }

    @Test
    @DisplayName("Should get score successfully")
    void testGetScoreSuccess() throws Exception {
        // Arrange
        List<Score> scores = new ArrayList<>();
        scores.add(score);
        when(scoreRepository.findAll()).thenReturn(scores);

        // Act
        Score result = scoreService.getScore();

        // Assert
        assertNotNull(result);
        assertEquals(100f, result.getValue());
        verify(scoreRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when trying to get score and none exists")
    void testGetScoreNotFound() {
        // Arrange
        when(scoreRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            scoreService.getScore();
        });

        assertEquals("Not found score, please add one.", exception.getMessage());
        verify(scoreRepository, times(1)).findAll();
    }
}
