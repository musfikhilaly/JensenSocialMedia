package org.example.jensensocialmedia.mapper;

import org.example.jensensocialmedia.dto.post.PostSummaryDTO;
import org.example.jensensocialmedia.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {
    @Mapping(target = "text", source = "post.content")
    PostSummaryDTO toPostSummaryDTO(Post post);
}
