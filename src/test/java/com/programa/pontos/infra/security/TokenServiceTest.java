package com.programa.pontos.infra.security;

import com.programa.pontos.model.User;
import com.programa.pontos.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenService Tests")
class TokenServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private TokenService tokenService;

    private User user;
    private String secret = "my-secret-key-for-testing-jwt-tokens";

    @BeforeEach
    void setUp() {
        user = new User(1, "John Doe", "12345678900", "john@example.com", 100f, "hashedPassword", "johndoe");
        ReflectionTestUtils.setField(tokenService, "secret", secret);
    }

    @Test
    @DisplayName("Should generate token successfully")
    void testGenerateTokenSuccess() {
        // Arrange
        doNothing().when(userService).saveToken(any(User.class), any(String.class));

        // Act
        String token = tokenService.generateToken(user);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        verify(userService, times(1)).saveToken(any(User.class), any(String.class));
    }

    @Test
    @DisplayName("Should validate token successfully")
    void testValidateTokenSuccess() {
        // Arrange
        String token = tokenService.generateToken(user);
        when(userService.getUserByUsername("johndoe")).thenReturn(user);
        user.setToken(token);

        // Act
        String result = tokenService.validateToken(token);

        // Assert
        assertEquals("johndoe", result);
    }

    @Test
    @DisplayName("Should return empty string when token is invalid")
    void testValidateTokenInvalid() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        String result = tokenService.validateToken(invalidToken);

        // Assert
        assertEquals("", result);
    }

    @Test
    @DisplayName("Should return empty string when token is malformed")
    void testValidateTokenMalformed() {
        // Arrange
        String malformedToken = "malformed";

        // Act
        String result = tokenService.validateToken(malformedToken);

        // Assert
        assertEquals("", result);
    }

    @Test
    @DisplayName("Should return empty string when user token does not match")
    void testValidateTokenMismatch() {
        // Arrange
        String token = tokenService.generateToken(user);
        user.setToken("different-token");
        when(userService.getUserByUsername("johndoe")).thenReturn(user);

        // Act
        String result = tokenService.validateToken(token);

        // Assert
        assertEquals("", result);
    }
}
