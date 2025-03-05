package space.space_spring.domain.post.adapter.out.persistence.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataLikeRepository extends JpaRepository<LikeJpaEntity, Long> {

    @Query("SELECT new space.space_spring.domain.post.adapter.out.persistence.like.PostLikeCount(l.postBase.id, COUNT(l)) " +
            "FROM LikeJpaEntity l " +
            "WHERE l.postBase.id IN :postIds " +
            "AND l.isLiked = true " +
            "GROUP BY l.postBase.id")
    List<PostLikeCount> countLikesByPostIds(@Param("postIds") List<Long> postIds);
}
