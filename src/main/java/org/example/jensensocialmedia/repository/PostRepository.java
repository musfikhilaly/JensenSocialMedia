package org.example.jensensocialmedia.repository;

import org.example.jensensocialmedia.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // find all posts by a specific user ID
    List<Post> findByUserId(Long userId);


    // find all posts sorted by creation time (newest first)
    List<Post> findAllByOrderByCreatedAtDesc();
}