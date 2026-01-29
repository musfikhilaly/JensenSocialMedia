package org.example.jensensocialmedia.repository;

import org.example.jensensocialmedia.dto.comment.CommentDetailResponse;
import org.example.jensensocialmedia.dto.comment.CommentReplyResponse;
import org.example.jensensocialmedia.model.Comment;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
                SELECT new org.example.jensensocialmedia.dto.comment.CommentDetailResponse(
                    c.id, c.content, c.createdAt, u.id, u.displayName, c.parentComment.id, COUNT(DISTINCT child.id)
                    )
                FROM Comment c
                JOIN c.user u
                LEFT JOIN Comment child ON child.parentComment = c
                WHERE c.post.id = :postId
                GROUP BY c.id, u.id
                ORDER BY c.createdAt ASC
            """)
    @Nullable
    Slice<CommentDetailResponse> findCommentsByPostId(@Param("postId") Long postId, Pageable pageable);

    @Nullable
    @Query("""
                SELECT new org.example.jensensocialmedia.dto.comment.CommentReplyResponse(
                    c.id, c.content, c.createdAt, u.id, u.displayName, COUNT(DISTINCT child.id)
                    )
                FROM Comment c
                JOIN c.user u
                LEFT JOIN Comment child ON child.parentComment = c
                WHERE child.parentComment.id = :parentId
                AND c.deleted = false
                GROUP BY c.id, u.id
                ORDER BY c.createdAt ASC                
            """)
    List<CommentReplyResponse> findByParentId(@Param("parentId") Long parentId);

    @Query("""
                SELECT new org.example.jensensocialmedia.dto.comment.CommentDetailResponse(
                    c.id, c.content, c.createdAt, u.id, u.displayName, c.parentComment.id,COUNT(DISTINCT child.id)
                    )
                FROM Comment c
                JOIN c.user u
                LEFT JOIN Comment child ON child.parentComment = c
                WHERE c.id = :commentId
                GROUP BY c.id, u.id
            """)
    CommentDetailResponse findCommentById(@Param("commentId") Long commentId);
}
