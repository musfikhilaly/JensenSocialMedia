package org.example.jensensocialmedia.mapper;

import org.example.jensensocialmedia.dto.comment.CommentCreateRequest;
import org.example.jensensocialmedia.dto.comment.CommentCreateResponse;
import org.example.jensensocialmedia.dto.comment.CommentReplyResponse;
import org.example.jensensocialmedia.dto.comment.CommentUpdateResponse;
import org.example.jensensocialmedia.model.Comment;
import org.example.jensensocialmedia.model.Post;
import org.example.jensensocialmedia.model.User;
import org.jspecify.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "content", source = "request.text")
    @Mapping(target = "user", source = "author")
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "deleted", expression = "java(false)")
    @Mapping(target = "edited", expression = "java(false)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment createRequestToComment(Post post, User author, CommentCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "content", source = "request.text")
    @Mapping(target = "user", source = "author")
    @Mapping(target = "parentComment", source = "parent")
    @Mapping(target = "deleted", expression = "java(false)")
    @Mapping(target = "edited", expression = "java(false)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment createRequestToComment(Post post, User author, Comment parent, CommentCreateRequest request);

    @Mapping(target = "authorId", source = "comment.user.id")
    @Mapping(target = "authorDisplayName", source = "comment.user.displayName")
    @Mapping(target = "postId", source = "comment.post.id")
    CommentCreateResponse toCreateResponse(Comment comment);


    CommentReplyResponse toCommentReplyResponse(Comment comment);

    @Nullable
    CommentUpdateResponse toCommentUpdateResponse(Comment currentComment);
}
