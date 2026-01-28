package org.example.jensensocialmedia.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jensensocialmedia.dto.comment.*;
import org.example.jensensocialmedia.exception.CommentNotFoundException;
import org.example.jensensocialmedia.exception.PostNotFoundException;
import org.example.jensensocialmedia.exception.UserNotFoundException;
import org.example.jensensocialmedia.mapper.CommentMapper;
import org.example.jensensocialmedia.model.Comment;
import org.example.jensensocialmedia.model.Post;
import org.example.jensensocialmedia.model.User;
import org.example.jensensocialmedia.repository.CommentRepository;
import org.example.jensensocialmedia.repository.PostRepository;
import org.example.jensensocialmedia.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    public CommentCreateResponse createComment(Long postId, Long userId, CommentCreateRequest request) {
        // Validate ID
        if (postId == null || postId <= 0) {
            throw new IllegalArgumentException("Invalid post id");
        } else if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }
        User author = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Author doesn't exist with id=" + userId));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post doesn't exist with id=" + postId));
        Comment comment;
        // If there is a parent comment
        if (request.parentId() != null) {
            Long parentId = request.parentId();
            if (parentId <= 0) {
                throw new IllegalArgumentException("Invalid parent id");
            }
            Comment parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new CommentNotFoundException("Parent Comment doesn't exist"));
            comment = commentMapper.createRequestToComment(post, author, parent, request);
        } else {
            comment = commentMapper.createRequestToComment(post, author, request);
        }
        commentRepository.save(comment);
        return commentMapper.toCreateResponse(comment);
    }

    public @Nullable Slice<CommentDetailResponse> getPostComments(Long postId, @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // Validate ID
        if (postId == null || postId <= 0) {
            throw new IllegalArgumentException("Invalid post id");
        }
        return commentRepository.findCommentsByPostId(postId, pageable);
    }

    public @Nullable List<CommentReplyResponse> getReplyComments(Long parentId) {
        // Validate ID
        if (parentId == null || parentId <= 0) {
            throw new IllegalArgumentException("Invalid parent comment id");
        }
        return commentRepository.findByParentId(parentId);
    }

    public @Nullable CommentDetailResponse updateComment(Long commentId, CommentUpdateRequest request, Long userId) {
        Comment currentComment = isCorrectAuthor(commentId, userId);
        // Update content and updateAt
        currentComment.setContent(request.content());
        currentComment.setUpdatedAt(Instant.now());
        commentRepository.save(currentComment);
        return commentRepository.findCommentById(commentId);
    }

    public void deleteComment(Long commentId, Long userId) {
        isCorrectAuthor(commentId, userId);
        commentRepository.deleteById(commentId);
    }

    private Comment isCorrectAuthor(Long commentId, Long userId) {
        // Validate ID
        if (commentId == null || commentId <= 0) {
            throw new IllegalArgumentException("Invalid comment id");
        } else if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }
        // Find the comment
        Comment currentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new PostNotFoundException("The comment doesn't exist"));
        // Check the author of the comment
        if (!currentComment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Wrong user to update this comment commentId=" + commentId);
        }
        return currentComment;
    }

}
