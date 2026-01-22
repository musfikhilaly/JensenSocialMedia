package org.example.jensensocialmedia.dto.post;

import java.time.Instant;

public record CreatePostResponse(Long id, String content, Instant createdAt) {


}
