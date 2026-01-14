package org.example.jensensocialmedia.dto.user;

public record UserProfileResponse(Long id, String username, String displayName, String bio, String avatarUrl) {
}
