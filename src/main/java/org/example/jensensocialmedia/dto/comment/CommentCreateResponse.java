package org.example.jensensocialmedia.dto.comment;

import java.time.Instant;

public record CommentCreateResponse(Long id,
                                    String content,
                                    Instant createdAt,
                                    Long authorId,
                                    String authorDisplayName,
                                    Long postId,
                                    long commentCount) {
}
