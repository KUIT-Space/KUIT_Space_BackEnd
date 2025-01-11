package space.space_spring.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.board.model.entity.Comment;
import space.space_spring.domain.board.model.entity.Post;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 댓글을 가져오는 메서드
    List<Comment> findByPost(Post post);

    // 대댓글 수를 계산하는 메서드
    int countByTargetId(Long targetId);

    // 유저가 특정 댓글을 좋아요 했는지 확인하는 메서드
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM CommentLike l WHERE l.comment.commentId = :commentId AND l.user.userId = :userId")
    boolean isUserLikedComment(@Param("commentId") Long commentId, @Param("userId") Long userId);

}
