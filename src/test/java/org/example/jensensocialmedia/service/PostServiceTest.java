package org.example.jensensocialmedia.service;

import org.example.jensensocialmedia.dto.post.CreatePostRequest;
import org.example.jensensocialmedia.dto.post.CreatePostResponse;
import org.example.jensensocialmedia.model.Post;
import org.example.jensensocialmedia.model.User;
import org.example.jensensocialmedia.repository.PostRepository;
import org.example.jensensocialmedia.repository.UserRepository;
import org.example.jensensocialmedia.util.CurrentUserProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private PostService postService;


    @Test
    @DisplayName("Test getFeed method")
    void getFeedSuccess() {
        // Arrange
        Instant now = Instant.now();
        Instant later = now.plusSeconds(60);

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Emil");

        Post post1 = new Post();
        post1.setId(1L);
        post1.setUser(user1);
        post1.setContent("First post");
        post1.setCreatedAt(now);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setUser(user1);
        post2.setContent("Second post");
        post2.setCreatedAt(later);

        List<Post> posts = List.of(post1, post2);
        when(postRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(posts);

        // Act
        List<CreatePostResponse> feed = postService.getFeed();

        // Assert
        assert (feed.size() == 2);
        assert (feed.get(0).id().equals(1L)); // Newest post first
        assert (feed.get(1).id().equals(2L));
    }

    @Test
    @DisplayName("Test createPost method")
    void createPostSuccess() {
        // Arrange
        Instant now = Instant.now();

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Emil");

        Post post = new Post();
        post.setUser(user1);
        post.setContent("First post");
        post.setCreatedAt(now);

        CreatePostRequest request = new CreatePostRequest("First post");

        when(currentUserProvider.getCurrentUserId())
                .thenReturn(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user1));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post p = invocation.getArgument(0);
            p.setId(1L); // Simulate DB assigning ID
            return p; // return the same post object
        });

        // Act
        CreatePostResponse response = postService.createPost(request);

        // Assert
        assert (response.id().equals(1L));
        assert (response.content().equals("First post"));
    }

    @Test
    @DisplayName("Test createPost method failure")
    void createPostFail() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Emil");

        CreatePostRequest request = new CreatePostRequest("First post");

        when(currentUserProvider.getCurrentUserId())
                .thenReturn(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> postService.createPost(request));
    }

    @Test
    @DisplayName("Test createPost method failure with invalid current user id")
    void createPostFailWithInvalidCurrentUserId() {
        // Arrange
        CreatePostRequest request = new CreatePostRequest("First post");

        when(currentUserProvider.getCurrentUserId())
                .thenReturn(0L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postService.createPost(request));
    }

    @Test
    @DisplayName("Test createPost method failure with invalid current user id null")
    void createPostFailWithInvalidCurrentUserIdNull() {
        // Arrange
        CreatePostRequest request = new CreatePostRequest("First post");

        when(currentUserProvider.getCurrentUserId())
                .thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postService.createPost(request));
    }

    @Test
    @DisplayName("Test getMyPosts method")
    void getMyPostsSuccess() {
        // Arrange
        Instant now = Instant.now();
        Instant later = now.plusSeconds(60);

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Emil");

        Post post1 = new Post();
        post1.setId(1L);
        post1.setUser(user1);
        post1.setContent("First post");
        post1.setCreatedAt(now);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setUser(user1);
        post2.setContent("Second post");
        post2.setCreatedAt(later);

        List<Post> posts = List.of(post1, post2);
        when(currentUserProvider.getCurrentUserId())
                .thenReturn(1L);
        when(postRepository.findByUserId(1L))
                .thenReturn(posts);

        // Act
        List<CreatePostResponse> myPosts = postService.getMyPosts();

        // Assert
        assert (myPosts.size() == 2);
        assert (myPosts.get(0).id().equals(2L)); // Newest post first
        assert (myPosts.get(1).id().equals(1L));
    }

    @Test
    @DisplayName("Test updatePost method")
    void updatePostSuccess() {
        // Arrange
        Instant now = Instant.now();

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Emil");

        Post post = new Post();
        post.setId(1L);
        post.setUser(user1);
        post.setContent("First post");
        post.setCreatedAt(now);

        when(currentUserProvider.getCurrentUserId())
                .thenReturn(1L);
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CreatePostRequest updateRequest = new CreatePostRequest("Updated content");

        // Act
        CreatePostResponse response = postService.updatePost(1L, updateRequest);

        // Assert
        assert (response.id().equals(1L));
        assert (response.content().equals("Updated content"));
    }

    @Test
    @DisplayName("Test updatePost method failure with different user")
    void updatePostFailWithDifferentUser() {
        // Arrange
        Instant now = Instant.now();

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Emil");

        Post post = new Post();
        post.setId(1L);
        post.setUser(user1);
        post.setContent("First post");
        post.setCreatedAt(now);

        when(currentUserProvider.getCurrentUserId())
                .thenReturn(2L); // Different user
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));
        CreatePostRequest updateRequest = new CreatePostRequest("Updated content");

        // Act & Assert
        assertThrows(SecurityException.class, () -> postService.updatePost(1L, updateRequest));
    }

    @Test
    @DisplayName("Test updatePost method failure with invalid id")
    void updatePostFailWithInvalidPostId() {
        // Arrange
        when(currentUserProvider.getCurrentUserId())
                .thenReturn(2L);
        CreatePostRequest updateRequest = new CreatePostRequest("Updated content");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postService.updatePost(0L, updateRequest));
    }

    @Test
    @DisplayName("Test updatePost method failure with id value null")
    void updatePostFailWithIdValueNull() {
        // Arrange
        when(currentUserProvider.getCurrentUserId())
                .thenReturn(2L);
        CreatePostRequest updateRequest = new CreatePostRequest("Updated content");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postService.updatePost(null, updateRequest));
    }

    @Test
    @DisplayName("Test deletePost method")
    void deletePostSuccess() {
        // Arrange
        Instant now = Instant.now();

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Emil");

        Post post = new Post();
        post.setId(1L);
        post.setUser(user1);
        post.setContent("First post");
        post.setCreatedAt(now);

        when(currentUserProvider.getCurrentUserId())
                .thenReturn(1L);
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        // Act
        postService.deletePost(1L);

        // Assert
        // No exception means success
    }

    @Test
    @DisplayName("Test deletePost method failure")
    void deletePostFail() {
        // Arrange
        Instant now = Instant.now();

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Emil");

        Post post = new Post();
        post.setId(1L);
        post.setUser(user1);
        post.setContent("First post");
        post.setCreatedAt(now);

        when(currentUserProvider.getCurrentUserId())
                .thenReturn(2L); // Different user
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        // Act & Assert
        assertThrows(SecurityException.class, () -> postService.deletePost(1L));
    }
}