package org.example.jensensocialmedia.dto.comment;

public record CommentCreateRequest(String text, Long parentId) {
}
