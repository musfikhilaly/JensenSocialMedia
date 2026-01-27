package org.example.jensensocialmedia.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = "Username can only contain letters, numbers, and spaces")
        String username,

        @NotBlank(message = "Email cannot be empty")
        @Size(min = 3, max = 254, message = "Email must be between 3 and 254 characters")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 40, message = "Password must be between 8 and 40 characters")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "Password must be at least 8 characters long, include one uppercase letter, one lowercase letter, and one number")
        String password,

        @NotBlank(message = "Display name cannot be empty")
        @Size(min = 3, max = 50, message = "Display name must be between 3 and 50 characters")
        @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = "Display name can only contain letters, numbers, and spaces")
        String displayName,

        @Size(max = 160, message = "Bio must be at most 160 characters")
        String bio,

        @Size(max = 255, message = "Profile image path too long")
        @Pattern(regexp = "^[A-Za-z0-9._\\-/]+$", message = "Invalid characters in profile image path")
        String profileImagePath
) {
}
