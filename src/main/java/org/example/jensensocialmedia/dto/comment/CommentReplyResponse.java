package org.example.jensensocialmedia.dto.comment;

import java.time.Instant;

public record CommentReplyResponse(Long id,
                                   String content,
                                   Instant createdAt,
                                   Long authorId,
                                   String displayUsername,
                                   long commentCount) {
}
