package com.programa.pontos.infra.security;

import com.programa.pontos.model.User;
import com.programa.pontos.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityFilter Tests")
class SecurityFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private SecurityFilter securityFilter;

    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        user = new User(1, "John Doe", "12345678900", "john@example.com", 100f, "hashedPassword", "johndoe");
        token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.example";
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should authenticate user when valid token is provided")
    void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(token);
        when(tokenService.validateToken(anyString())).thenReturn("johndoe");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        assertEquals(user, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(request, times(1)).getHeader("Authorization");
        verify(tokenService, times(1)).validateToken(anyString());
        verify(userRepository, times(1)).findByUsername("johndoe");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should continue filter chain when no token is provided")
    void testDoFilterInternalWithoutToken() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(request, times(1)).getHeader("Authorization");
        verify(tokenService, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle missing Authorization header gracefully")
    void testRecoverTokenWhenHeaderMissing() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(tokenService, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
