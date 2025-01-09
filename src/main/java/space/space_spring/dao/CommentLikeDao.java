package space.space_spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.Comment;
import space.space_spring.entity.CommentLike;
import space.space_spring.domain.user.model.entity.User;

import java.util.Optional;

@Repository
public interface CommentLikeDao extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
