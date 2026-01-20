package org.example.jensensocialmedia.service;

import org.example.jensensocialmedia.dto.user.UserProfileResponse;
import org.example.jensensocialmedia.mapper.UserMapper;
import org.example.jensensocialmedia.model.User;
import org.example.jensensocialmedia.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;


    @InjectMocks
    private UserService userService;

    // Test 1: getAllUsers
    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        User user = new User();
        UserProfileResponse response =
                new UserProfileResponse(1L, "Elice", "elice", null, null);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserProfileResponse(user)).thenReturn(response);

        List<UserProfileResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }
}

