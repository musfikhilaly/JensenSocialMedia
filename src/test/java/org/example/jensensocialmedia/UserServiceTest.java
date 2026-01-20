package org.example.jensensocialmedia.service;

import org.example.jensensocialmedia.dto.user.CreateUserRequest;
import org.example.jensensocialmedia.dto.user.CreateUserResponse;
import org.example.jensensocialmedia.dto.user.UserProfileResponse;
import org.example.jensensocialmedia.mapper.UserMapper;
import org.example.jensensocialmedia.model.User;
import org.example.jensensocialmedia.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // Test 1: getAllUsers
    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        User user = new User();
        UserProfileResponse response =
                new UserProfileResponse(1L, "Alice", "Alice", null, null);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserProfileResponse(user)).thenReturn(response);

        List<UserProfileResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    // Test 2: findById
    @Test
    void findById_existingUser_shouldReturnUserProfile() {
        Long userId = 1L;
        User user = new User();
        UserProfileResponse response =
                new UserProfileResponse(userId, "Alice", "Alice", null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserProfileResponse(user)).thenReturn(response);

        UserProfileResponse result = userService.findById(userId);

        assertEquals("Alice", result.username());
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_nonExistingUser_shouldThrowException() {
        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(userId));
        verify(userRepository).findById(userId);
    }

    // Test 3: createUser
    @Test
    void createUser_shouldReturnCreatedUserResponse() {
        CreateUserRequest request =
                new CreateUserRequest("Alice", "elice@example.com", "password123");
        User user = new User();
        User savedUser = new User();
        CreateUserResponse response =
                new CreateUserResponse(1L, "Alice");

        when(userMapper.fromCreateUserRequest(request)).thenReturn(user);
        lenient().when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toUserCreateUserResponse(savedUser)).thenReturn(response);

        CreateUserResponse result = userService.createUser(request);

        assertEquals("Alice", result.username());
        assertEquals(1L, result.id());
        verify(userRepository).save(user);
    }

}

