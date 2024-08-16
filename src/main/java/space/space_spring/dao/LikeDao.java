package space.space_spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.entity.Post;
import space.space_spring.entity.PostLike;
import space.space_spring.entity.User;

import java.util.Optional;

public interface LikeDao extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
