package org.example.jensensocialmedia.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jensensocialmedia.dto.post.PostSummaryDTO;
import org.example.jensensocialmedia.dto.user.*;
import org.example.jensensocialmedia.exception.UserNotFoundException;
import org.example.jensensocialmedia.mapper.PostMapper;
import org.example.jensensocialmedia.mapper.UserMapper;
import org.example.jensensocialmedia.model.Post;
import org.example.jensensocialmedia.model.User;
import org.example.jensensocialmedia.repository.PostRepository;
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
    private final PostRepository postRepository;
    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserProfileResponse> getAllUsers() {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Fetching all users from the database");
        List<User> users = userRepository.findAll();
        log.info("Mapping {} users to UserProfileResponse DTOs", users.size());
        return users.stream()
                .map(userMapper::toUserProfileResponse)
                .toList();
    }

    public UserProfileResponse findById(Long id) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Fetching user with ID: {}", id);
        if (id == null || id <= 0) {
            log.warn("Invalid user ID: {}", id);
            throw new IllegalArgumentException("Current user ID cannot be null or less than or equal to zero");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("Mapping user to UserProfileResponse DTO");
        return userMapper.toUserProfileResponse(user);
    }

    public CreateUserResponse createUser(CreateUserRequest request) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Creating user with username: {}", request.username());
        User user = userMapper.fromCreateUserRequest(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());
        return userMapper.toUserCreateUserResponse(savedUser);
    }

    public UserProfileResponse updateProfile(Long currentUserId, UserUpdateProfileRequest request) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Updating profile for user ID: {}", currentUserId);
        if (currentUserId == null || currentUserId <= 0) {
            log.warn("Invalid currentUserId: {}", currentUserId);
            throw new IllegalArgumentException("Current user ID cannot be null or less than or equal to zero");
        }
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("Updating user profile fields");
        user.setDisplayName(request.displayName());
        user.setBio(HtmlSanitizer.sanitize(request.bio()));
        user.setAvatarUrl(request.avatarUrl());
        User updatedUser = userRepository.save(user);
        log.info("User profile updated for user ID: {}", currentUserId);
        return userMapper.toUserProfileResponse(updatedUser);
    }

    public void deleteUser(Long currentUserId) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Deleting user with ID: {}", currentUserId);
        if (currentUserId == null || currentUserId <= 0) {
            log.warn("Invalid currentUserId: {}", currentUserId);
            throw new IllegalArgumentException("Current user ID cannot be null or less than or equal to zero");
        }
        if (!userRepository.existsById(currentUserId)) {
            log.info("User with ID: {} not found", currentUserId);
            throw new UserNotFoundException("User not found");
        }
        log.info("User found, proceeding to delete user ID: {}", currentUserId);
        userRepository.deleteById(currentUserId);
    }

    public UserWithPostsResponseDTO getUserWithPosts(Long id) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Fetching user with posts for user ID: {}", id);
        if (id == null || id <= 0) {
            log.warn("Invalid user ID: {}", id);
            throw new IllegalArgumentException("User ID cannot be null or less than or equal to zero");
        }
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("Fetching posts for user ID: {}", id);
        List<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        log.info("Mapping user and posts to DTOs");
        UserSummaryDTO userSummaryDTO = userMapper.toUserSummaryDTO(user);
        List<PostSummaryDTO> postSummaryDTOs = posts.stream()
                .map(postMapper::toPostSummaryDTO)
                .toList();
        log.info("Returning UserWithPostsResponseDTO");
        return new UserWithPostsResponseDTO(userSummaryDTO, postSummaryDTOs);
    }
}
