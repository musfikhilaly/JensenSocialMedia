package org.example.jensensocialmedia.service;

import org.example.jensensocialmedia.model.SecurityUser;
import org.example.jensensocialmedia.model.User;
import org.example.jensensocialmedia.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("Test loadUserByUsername with valid username")
    void testLoadUserByUsername_ValidUsername() {
        // Arrange
        String username = "validUser";
        User user = User.builder()
                .id(1L)
                .username("Emil")
                .password("Password123")
                .build();
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        // Act
        SecurityUser userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(user.getId(), userDetails.getId());
    }

    @Test
    @DisplayName("Test loadUserByUsername with invalid username")
    void testLoadUserByUsername_InvalidUsername() {
        // Arrange
        String username = "invalidUser";
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(username));
    }
}