package com.programa.pontos.service;

import com.programa.pontos.dtos.AddStoreServiceDTO;
import com.programa.pontos.dtos.UserDTO;
import com.programa.pontos.model.Score;
import com.programa.pontos.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AddScoreService Tests")
class AddScoreServiceTest {

    @Mock
    private ScoreService scoreService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AddScoreService addScoreService;

    private User user;
    private Score score;
    private AddStoreServiceDTO addStoreServiceDTO;

    @BeforeEach
    void setUp() {
        user = new User(1, "John Doe", "12345678900", "john@example.com", 100f, "hashedPassword", "johndoe");
        score = new Score(1, 200f);
        addStoreServiceDTO = new AddStoreServiceDTO("12345678900", 50f);
    }

    @Test
    @DisplayName("Should add score to user successfully")
    void testSetStoreServiceSuccess() throws Exception {
        // Arrange
        when(userService.findUserByDocument("12345678900")).thenReturn(Optional.of(user));
        when(userService.updateUser(anyInt(), any(UserDTO.class))).thenReturn(user);

        // Act
        User result = addScoreService.setStoreService(addStoreServiceDTO);

        // Assert
        assertNotNull(result);
        verify(userService, times(1)).findUserByDocument("12345678900");
        verify(userService, times(1)).updateUser(anyInt(), any(UserDTO.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testSetStoreServiceUserNotFound() throws Exception {
        // Arrange
        String documentNotFound = "99999999999";
        when(userService.findUserByDocument(documentNotFound)).thenReturn(Optional.empty());

        AddStoreServiceDTO dto = new AddStoreServiceDTO(documentNotFound, 50f);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            addScoreService.setStoreService(dto);
        });

        assertEquals("User not found.", exception.getMessage());
        verify(userService, times(1)).findUserByDocument(documentNotFound);
        verify(userService, never()).updateUser(anyInt(), any(UserDTO.class));
    }

    @Test
    @DisplayName("Should validate score and reset user score when score is reached")
    void testValidationScoreReached() throws Exception {
        // Arrange
        User userWithHighScore = new User(1, "John Doe", "12345678900", "john@example.com", 250f, "hashedPassword", "johndoe");
        when(scoreService.getScore()).thenReturn(score);
        when(userService.updateUser(anyInt(), any(UserDTO.class))).thenReturn(userWithHighScore);

        // Act
        boolean result = addScoreService.validationScore(userWithHighScore);

        // Assert
        assertTrue(result);
        verify(scoreService, times(1)).getScore();
        verify(userService, times(1)).updateUser(anyInt(), any(UserDTO.class));
    }

    @Test
    @DisplayName("Should validate score and return false when score is not reached")
    void testValidationScoreNotReached() throws Exception {
        // Arrange
        when(scoreService.getScore()).thenReturn(score);

        // Act
        boolean result = addScoreService.validationScore(user);

        // Assert
        assertFalse(result);
        verify(scoreService, times(1)).getScore();
        verify(userService, never()).updateUser(anyInt(), any(UserDTO.class));
    }

    @Test
    @DisplayName("Should validate score and reset score to zero when threshold is reached")
    void testValidationScoreExactThreshold() throws Exception {
        // Arrange
        User userWithExactScore = new User(1, "John Doe", "12345678900", "john@example.com", 200f, "hashedPassword", "johndoe");
        when(scoreService.getScore()).thenReturn(score);
        when(userService.updateUser(anyInt(), any(UserDTO.class))).thenReturn(userWithExactScore);

        // Act
        boolean result = addScoreService.validationScore(userWithExactScore);

        // Assert
        assertTrue(result);
        verify(scoreService, times(1)).getScore();
        verify(userService, times(1)).updateUser(anyInt(), any(UserDTO.class));
    }
}
