package org.example.jensensocialmedia.dto.comment;

import java.time.Instant;

public record CommentUpdateResponse(Long id,
                                    String content,
                                    Instant createdAt,
                                    Long authorId,
                                    String displayName,
                                    long commentCount) {
}
