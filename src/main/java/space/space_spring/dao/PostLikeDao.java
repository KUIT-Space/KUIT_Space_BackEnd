package space.space_spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.entity.*;

import java.util.Optional;

public interface PostLikeDao extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
