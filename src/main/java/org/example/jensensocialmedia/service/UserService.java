package org.example.jensensocialmedia.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jensensocialmedia.dto.user.CreateUserRequest;
import org.example.jensensocialmedia.dto.user.CreateUserResponse;
import org.example.jensensocialmedia.dto.user.UserProfileResponse;
import org.example.jensensocialmedia.dto.user.UserUpdateProfileRequest;
import org.example.jensensocialmedia.exception.UserNotFoundException;
import org.example.jensensocialmedia.mapper.UserMapper;
import org.example.jensensocialmedia.model.User;
import org.example.jensensocialmedia.repository.UserRepository;
import org.example.jensensocialmedia.util.HtmlSanitizer;
import org.slf4j.MDC;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing user-related operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserProfileResponse> getAllUsers() {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Fetching all users from the database");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserProfileResponse)
                .toList();
    }

    public UserProfileResponse findById(Long id) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Fetching user with ID: {}", id);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Current user ID cannot be null or less than or equal to zero");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserProfileResponse(user);
    }

    public CreateUserResponse createUser(CreateUserRequest request) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        User user = userMapper.fromCreateUserRequest(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        log.info("Creating new user with username: {}", user.getUsername());
        User savedUser = userRepository.save(user);
        return userMapper.toUserCreateUserResponse(savedUser);
    }

    public UserProfileResponse updateProfile(Long currentUserId, UserUpdateProfileRequest request) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Updating profile for user ID: {}", currentUserId);
        if (currentUserId == null || currentUserId <= 0) {
            throw new IllegalArgumentException("Current user ID cannot be null or less than or equal to zero");
        }
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setDisplayName(request.displayName());
        user.setBio(HtmlSanitizer.sanitize(request.bio()));
        user.setAvatarUrl(request.avatarUrl());
        User updatedUser = userRepository.save(user);
        return userMapper.toUserProfileResponse(updatedUser);
    }

    public void deleteUser(Long currentUserId) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Deleting user with ID: {}", currentUserId);
        if (currentUserId == null || currentUserId <= 0) {
            throw new IllegalArgumentException("Current user ID cannot be null or less than or equal to zero");
        }
        if (!userRepository.existsById(currentUserId)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(currentUserId);
    }
}
