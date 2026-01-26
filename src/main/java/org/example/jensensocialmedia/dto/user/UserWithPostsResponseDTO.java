package org.example.jensensocialmedia.dto.user;

import org.example.jensensocialmedia.dto.post.PostSummaryDTO;

import java.util.List;

public record UserWithPostsResponseDTO(UserSummaryDTO user, List<PostSummaryDTO> posts) {
}
