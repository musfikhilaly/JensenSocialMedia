package org.example.jensensocialmedia.dto.post;

import java.time.Instant;

public record PostSummaryDTO(Long id, String text, Instant createdAt) {
}
