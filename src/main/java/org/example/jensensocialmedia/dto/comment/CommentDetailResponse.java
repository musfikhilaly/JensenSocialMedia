package org.example.jensensocialmedia.dto.comment;

import java.time.Instant;

public record CommentDetailResponse(Long id,
                                    String content,
                                    Instant createdAt,
                                    Long authorId,
                                    String displayName,
                                    Long parentId,
                                    long commentCount) {
}
