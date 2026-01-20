package org.example.jensensocialmedia.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jensensocialmedia.dto.post.CreatePostRequest;
import org.example.jensensocialmedia.dto.post.CreatePostResponse;
import org.example.jensensocialmedia.exception.UserNotFoundException;
import org.example.jensensocialmedia.model.Post;
import org.example.jensensocialmedia.model.User;
import org.example.jensensocialmedia.repository.PostRepository;
import org.example.jensensocialmedia.repository.UserRepository;
import org.example.jensensocialmedia.util.CurrentUserProvider;
import org.example.jensensocialmedia.util.HtmlSanitizer;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;

// all posts
    public List<CreatePostResponse> getFeed() {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Fetching public feed (newest first)");
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Create post

    public CreatePostResponse createPost(@Valid CreatePostRequest request) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        Long currentUserId = requireCurrentUserId();
        log.info("Creating post for currentUserId={}", currentUserId);

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String safeContent = HtmlSanitizer.sanitize(request.content());

        Post post = Post.builder()
                .content(safeContent)
                .user(user)
                .build();

        Post saved = postRepository.save(post);
        return toResponse(saved);
    }

    // ========== Current user ("me") ==========

    public List<CreatePostResponse> getMyPosts() {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        Long currentUserId = requireCurrentUserId();
        log.info("Fetching posts for currentUserId={}", currentUserId);
        return postRepository.findByUserId(currentUserId)
                .stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(this::toResponse)
                .toList();
    }



    // ========== Update & Delete ==========
    //update
    public CreatePostResponse updatePost(Long postId, @Valid CreatePostRequest request) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        Long currentUserId = requireCurrentUserId();
        Long pid = requireId(postId);
        log.info("Updating post id={} by currentUserId={}", pid, currentUserId);

        Post post = postRepository.findById(pid)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new SecurityException("You are not allowed to update this post");
        }

        String safeContent = HtmlSanitizer.sanitize(request.content());
        post.setContent(safeContent);
        Post saved = postRepository.save(post);
        return toResponse(saved);
    }
//delete post
    public void deletePost(Long postId) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        Long currentUserId = requireCurrentUserId();
        Long pid = requireId(postId);
        log.info("Deleting post id={} by currentUserId={}", pid, currentUserId);

        Post post = postRepository.findById(pid)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new SecurityException("You are not allowed to delete this post");
        }
        postRepository.delete(post);
    }

    // ========== Helpers ==========

    private CreatePostResponse toResponse(Post post) {
        return new CreatePostResponse(
                post.getId(),
                post.getContent(),
                post.getCreatedAt()
        );
    }

    private Long requireId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be a positive number");
        }
        return id;
    }

    private Long requireCurrentUserId() {
        Long id = currentUserProvider.getCurrentUserId();
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Current user id must be a positive number");
        }
        return id;
    }
}