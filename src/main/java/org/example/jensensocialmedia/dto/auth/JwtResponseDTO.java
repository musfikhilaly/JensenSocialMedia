package org.example.jensensocialmedia.dto.auth;

public record JwtResponseDTO(String token, org.example.jensensocialmedia.dto.user.UserProfileResponse byId) {
}
