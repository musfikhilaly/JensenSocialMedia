package org.example.jensensocialmedia.dto.post;

import org.example.jensensocialmedia.model.User;

import java.time.Instant;

public record CreatePostResponse(Long id, String content, User user, Instant createdAt, Instant updatedAt
) {}
