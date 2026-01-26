package com.programa.pontos.infra.security;

import com.programa.pontos.model.User;
import com.programa.pontos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsServiceImp Tests")
class UserDetailsServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImp userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "John Doe", "12345678900", "john@example.com", 100f, "hashedPassword", "johndoe");
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void testLoadUserByUsernameSuccess() {
        // Arrange
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("johndoe");

        // Assert
        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        assertEquals("hashedPassword", result.getPassword());
        verify(userRepository, times(1)).findByUsername("johndoe");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void testLoadUserByUsernameNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("Should build UserDetails with correct authorities")
    void testLoadUserByUsernameAuthorities() {
        // Arrange
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("johndoe");

        // Assert
        assertNotNull(result.getAuthorities());
        verify(userRepository, times(1)).findByUsername("johndoe");
    }
}
