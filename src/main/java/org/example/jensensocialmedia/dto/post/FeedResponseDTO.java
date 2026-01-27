package org.example.jensensocialmedia.dto.post;

import org.example.jensensocialmedia.dto.user.UserInfoDTO;

import java.time.Instant;

public record FeedResponseDTO(Long id, String text, Instant createdAt, UserInfoDTO user) {
}
