package space.space_spring.domain.post.adapter.out.persistence.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataPostRepository extends JpaRepository<PostJpaEntity, Long>, QuerydslPredicateExecutor<PostJpaEntity> {
    @Query("SELECT p FROM PostJpaEntity p " +
            "JOIN FETCH p.postBase pb " +
            "WHERE pb.board.id = :boardId")
    List<PostJpaEntity> findPostsByBoardId(@Param("boardId") Long boardId);
}
