package space.space_spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.Post;
import space.space_spring.entity.Space;

import java.util.List;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {

    List<Post> findBySpace(Space space);

    List<Post> findBySpaceAndType(Space space, String type);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM PostLike l WHERE l.post.postId = :postId AND l.user.userId = :userId")
    boolean isUserLikedPost(@Param("postId") Long postId, @Param("userId") Long userId);

}
