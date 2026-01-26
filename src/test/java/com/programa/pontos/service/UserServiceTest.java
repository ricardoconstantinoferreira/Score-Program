package com.programa.pontos.service;

import com.programa.pontos.dtos.UserDTO;
import com.programa.pontos.model.User;
import com.programa.pontos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User(1, "John Doe", "12345678900", "john@example.com", 100f, "hashedPassword", "johndoe");
        userDTO = new UserDTO("John Doe", "12345678900", "john@example.com", 100f, "password123", "johndoe");
    }

    @Test
    @DisplayName("Should create a user successfully")
    void testCreateUserSuccess() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.createUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("12345678900", result.getDocument());
        assertEquals("john@example.com", result.getEmail());
        assertEquals(100f, result.getScore());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should save token to user successfully")
    void testSaveTokenSuccess() {
        // Arrange
        String token = "jwt_token_example";
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.saveToken(user, token);

        // Assert
        assertEquals(token, user.getToken());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUserSuccess() throws Exception {
        // Arrange
        UserDTO updateDTO = new UserDTO("Jane Doe", "", "jane@example.com", 150f, "", "");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.updateUser(1, updateDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to update non-existent user")
    void testUpdateUserNotFound() {
        // Arrange
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userService.updateUser(999, userDTO);
        });

        assertEquals("User not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(999);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should get all users successfully")
    void testGetAllUsersSuccess() throws Exception {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when no users exist")
    void testGetAllUsersEmpty() {
        // Arrange
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userService.getAllUsers();
        });

        assertEquals("Nobody Users", exception.getMessage());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get user by id successfully")
    void testGetUserByIdSuccess() throws Exception {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should throw exception when user id not found")
    void testGetUserByIdNotFound() {
        // Arrange
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userService.getUserById(999);
        });

        assertEquals("User with id 999 not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Should find user by document successfully")
    void testFindUserByDocumentSuccess() throws Exception {
        // Arrange
        when(userRepository.findUserByDocument("12345678900")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findUserByDocument("12345678900");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("12345678900", result.get().getDocument());
        verify(userRepository, times(1)).findUserByDocument("12345678900");
    }

    @Test
    @DisplayName("Should throw exception when user document not found")
    void testFindUserByDocumentNotFound() {
        // Arrange
        when(userRepository.findUserByDocument("99999999999")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userService.findUserByDocument("99999999999");
        });

        assertEquals("User with document 99999999999 not found.", exception.getMessage());
        verify(userRepository, times(1)).findUserByDocument("99999999999");
    }

    @Test
    @DisplayName("Should return true when user exists by document")
    void testHasUserTrue() {
        // Arrange
        when(userRepository.findUserByDocument("12345678900")).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.hasUser("12345678900");

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findUserByDocument("12345678900");
    }

    @Test
    @DisplayName("Should return false when user does not exist by document")
    void testHasUserFalse() {
        // Arrange
        when(userRepository.findUserByDocument("99999999999")).thenReturn(Optional.empty());

        // Act
        boolean result = userService.hasUser("99999999999");

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findUserByDocument("99999999999");
    }

    @Test
    @DisplayName("Should get user by username successfully")
    void testGetUserByUsernameSuccess() {
        // Arrange
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByUsername("johndoe");

        // Assert
        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(userRepository, times(1)).findByUsername("johndoe");
    }
}
