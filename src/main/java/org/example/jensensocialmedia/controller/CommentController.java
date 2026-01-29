package org.example.jensensocialmedia.controller;

import lombok.RequiredArgsConstructor;
import org.example.jensensocialmedia.dto.comment.*;
import org.example.jensensocialmedia.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment(@PathVariable Long postId, @RequestBody CommentCreateRequest request) {
        CommentCreateResponse response = commentService.createComment(postId, request);
        return ResponseEntity
                .created(URI.create("/posts/" + postId + "/comments/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Slice<CommentDetailResponse>> getPostComments(@PathVariable Long postId, Pageable pageable) {
        return ResponseEntity
                .ok()
                .body(commentService.getPostComments(postId, pageable));
    }

    @GetMapping("{commentId}")
    public ResponseEntity<List<CommentReplyResponse>> getReplyComments(@PathVariable Long commentId) {
        return ResponseEntity
                .ok()
                .body(commentService.getReplyComments(commentId));
    }

    @PutMapping("{commentId}")
    public ResponseEntity<CommentDetailResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        return ResponseEntity.ok().body(commentService.updateComment(commentId, request));
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
