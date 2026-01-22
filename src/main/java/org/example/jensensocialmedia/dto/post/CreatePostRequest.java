package org.example.jensensocialmedia.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @JsonProperty("text")
        @NotBlank(message = "Post cannot be empty")
        @Size(max = 500, message = "Post cant be longer than 500 characters")
        String content
) {
}





        /*
        Long id, String content, User user, Instant createdAt, Instant updatedAt
         */