package org.example.jensensocialmedia.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @NotBlank(message = "Post cannot be empty")
        @Size(max = 500, message = "Post cant be longer than 500 characters")
        String content,

        @NotNull(message = "User id is required")
        @Positive(message = "User id must be positive")
        Long userId
) {}





        /*
        Long id, String content, User user, Instant createdAt, Instant updatedAt
         */