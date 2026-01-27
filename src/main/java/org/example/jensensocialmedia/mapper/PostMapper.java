package org.example.jensensocialmedia.mapper;

import org.example.jensensocialmedia.dto.post.FeedResponseDTO;
import org.example.jensensocialmedia.dto.post.PostSummaryDTO;
import org.example.jensensocialmedia.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {
    @Mapping(target = "text", source = "post.content")
    @Mapping(target = "user.id", source = "post.user.id")
    @Mapping(target = "user.displayName", source = "post.user.displayName")
    PostSummaryDTO toPostSummaryDTO(Post post);

    @Mapping(target = "user.id", source = "post.user.id")
    @Mapping(target = "user.displayName", source = "post.user.displayName")
    @Mapping(target = "text", source = "post.content")
    FeedResponseDTO toFeedResponseDTO(Post post);
}
