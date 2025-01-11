package space.space_spring.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.board.model.entity.Post;
import space.space_spring.domain.board.model.entity.PostLike;
import space.space_spring.domain.user.model.entity.User;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
