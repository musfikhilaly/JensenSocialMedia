package org.example.jensensocialmedia.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jensensocialmedia.dto.post.CreatePostRequest;
import org.example.jensensocialmedia.dto.post.CreatePostResponse;
import org.example.jensensocialmedia.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // GET /posts — public feed
    @GetMapping("/posts")
    public List<CreatePostResponse> getFeed() {
        return postService.getFeed();
    }

    // POST /posts — create for current user
    @PostMapping("/posts")
    public ResponseEntity<CreatePostResponse> create(@RequestBody @Valid CreatePostRequest request) {
        CreatePostResponse response = postService.createPost(request);
        return ResponseEntity.created(URI.create("/posts/" + response.id())).body(response);
    }

    // GET /users/me/posts — my posts
    @GetMapping("/users/me/posts")
    public List<CreatePostResponse> myPosts() {
        return postService.getMyPosts();
    }

    // PUT /posts/{postId} — update (owner only)
    @PutMapping("/posts/{postId}")
    public CreatePostResponse update(@PathVariable Long postId,
                                     @RequestBody @Valid CreatePostRequest request) {
        return postService.updatePost(postId, request);
    }

    // DELETE /posts/{postId} — delete (owner only)
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}