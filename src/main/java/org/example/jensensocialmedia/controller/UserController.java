package org.example.jensensocialmedia.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jensensocialmedia.dto.user.CreateUserRequest;
import org.example.jensensocialmedia.dto.user.CreateUserResponse;
import org.example.jensensocialmedia.dto.user.UserProfileResponse;
import org.example.jensensocialmedia.dto.user.UserUpdateProfileRequest;
import org.example.jensensocialmedia.service.UserService;
import org.example.jensensocialmedia.util.CurrentUserProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {
        return ResponseEntity
                .ok()
                .body(userService.findById(currentUserProvider.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        CreateUserResponse response = userService.createUser(request);
        return ResponseEntity
                .created(URI.create("/users/" + response.id()))
                .body(response);
    }

    @PutMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(@RequestBody UserUpdateProfileRequest request) {
        return ResponseEntity
                .ok()
                .body(userService.updateProfile(currentUserProvider.getCurrentUserId(), request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount() {
        userService.deleteUser(currentUserProvider.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}
