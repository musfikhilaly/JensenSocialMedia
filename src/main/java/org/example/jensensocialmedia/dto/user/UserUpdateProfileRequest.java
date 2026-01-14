package org.example.jensensocialmedia.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateProfileRequest(
        @Size(min = 3, max = 30, message = "It must be between 3 - 30 letters")
        @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = "Only letters, numbers and space allowed")
        String displayName,

        @NotBlank(message = "It can't be empty")
        @Size(min = 1, max = 200, message = "It must be between 1 - 200 letters")
        @Pattern(regexp = "^[A-Za-z0-9 ,.]*$", message = "Only letters, numbers, space, \",\" and \".\" allowed")
        String bio,

        @Size(max = 30, message = "It must be max 30 letters")
        @Pattern(regexp = "^/(?:[^/\0]+/)*[^/\0]*$",
                message = "Only letters, numbers, space, \",\" and \".\" allowed")
        String avatarUrl) {
}
