package space.space_spring.domain.post.adapter.out.persistence.comment;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataPostCommentRepository extends JpaRepository<PostCommentJpaEntity, Long> {

    @Query("SELECT new space.space_spring.domain.post.adapter.out.persistence.comment.PostCommentCount(pc.postBase.id, COUNT(pc)) " +
            "FROM PostCommentJpaEntity pc " +
            "WHERE pc.postBase.id IN :postIds " +
            "GROUP BY pc.postBase.id")
    List<PostCommentCount> countCommentsByPostIds(@Param("postIds") List<Long> postIds);
}
