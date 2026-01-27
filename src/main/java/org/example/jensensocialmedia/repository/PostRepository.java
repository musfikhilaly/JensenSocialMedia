package org.example.jensensocialmedia.repository;

import org.example.jensensocialmedia.model.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Get all posts by user ID
    List<Post> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Get all posts by username
    List<Post> findByUserUsername(String username);

    // Get all posts ordered by creation time (from BaseEntity)
    @EntityGraph(attributePaths = {"user"})
    List<Post> findAllByOrderByCreatedAtDesc();
}
