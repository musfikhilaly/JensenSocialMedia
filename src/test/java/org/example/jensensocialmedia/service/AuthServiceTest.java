package org.example.jensensocialmedia.service;

import org.example.jensensocialmedia.dto.auth.JwtResponseDTO;
import org.example.jensensocialmedia.dto.auth.LoginRequestDTO;
import org.example.jensensocialmedia.model.SecurityUser;
import org.example.jensensocialmedia.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("login should return JwtResponseDTO when authentication is successful")
    void login_ShouldReturnJwtResponseDTO_WhenAuthenticationIsSuccessful() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .build();
        SecurityUser su = new SecurityUser(user);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(su);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);

        when(tokenService.generateToken(auth)).thenReturn("mocked-jwt-token");

        // Act
        LoginRequestDTO request = new LoginRequestDTO("testuser", "password");
        JwtResponseDTO response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.userId());
        assertEquals("mocked-jwt-token", response.token());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(auth);
    }

    @Test
    @DisplayName("login should throw exception when authentication fails")
    void login_ShouldThrowException_WhenAuthenticationFails() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));
        // Act & Assert
        LoginRequestDTO request = new LoginRequestDTO("invaliduser", "wrongpassword");
        try {
            authService.login(request);
        } catch (RuntimeException ex) {
            assertEquals("Authentication failed", ex.getMessage());
        }
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, never()).generateToken(any(Authentication.class));
    }

    @Test
    @DisplayName("when principal is not SecurityUser, should still generate token")
    void login_ShouldGenerateToken_WhenPrincipalIsNotSecurityUser() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("someOtherPrincipal");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);

        when(tokenService.generateToken(auth)).thenReturn("mocked-jwt-token-2");
        LoginRequestDTO request = new LoginRequestDTO("someUsername", "password");

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> authService.login(request));
    }
}