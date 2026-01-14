package org.example.jensensocialmedia.mapper;


import org.example.jensensocialmedia.dto.user.CreateUserRequest;
import org.example.jensensocialmedia.dto.user.CreateUserResponse;
import org.example.jensensocialmedia.dto.user.UserProfileResponse;
import org.example.jensensocialmedia.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserProfileResponse toUserProfileResponse(User user);

    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "displayName", source = "request.username")
    @Mapping(target = "bio", constant = "Hello World.")
    User fromCreateUserRequest(CreateUserRequest request);

    CreateUserResponse toUserCreateUserResponse(User savedUser);
}
