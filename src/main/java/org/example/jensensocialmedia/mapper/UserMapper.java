package org.example.jensensocialmedia.mapper;


import org.example.jensensocialmedia.dto.user.CreateUserRequest;
import org.example.jensensocialmedia.dto.user.CreateUserResponse;
import org.example.jensensocialmedia.dto.user.UserProfileResponse;
import org.example.jensensocialmedia.dto.user.UserSummaryDTO;
import org.example.jensensocialmedia.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface UserMapper {
    /**
     * Maps a User entity to a UserProfileResponse DTO.
     *
     * @param user the User entity to be mapped
     * @return the corresponding UserProfileResponse DTO
     */
    UserProfileResponse toUserProfileResponse(User user);

    /**
     * Maps a CreateUserRequest DTO to a User entity.
     *
     * @param request the CreateUserRequest DTO to be mapped
     * @return the corresponding User entity
     */
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "displayName", source = "displayName", defaultValue = "request.username")
    @Mapping(target = "bio", source = "bio", defaultValue = "Hello World.")
    @Mapping(target = "avatarUrl", source = "profileImagePath")
    User fromCreateUserRequest(CreateUserRequest request);

    /**
     * Maps a User entity to a CreateUserResponse DTO.
     *
     * @param savedUser the User entity to be mapped
     * @return the corresponding CreateUserResponse DTO
     */
    CreateUserResponse toUserCreateUserResponse(User savedUser);

    /**
     * Maps a User entity to a UserSummaryDTO.
     *
     * @param user the User entity to be mapped
     * @return the corresponding UserSummaryDTO
     */
    UserSummaryDTO toUserSummaryDTO(User user);
}
