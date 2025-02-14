package space.space_spring.domain.post.adapter.out.persistence.board;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;

public interface SpringDataBoardRepository extends JpaRepository<BoardJpaEntity, Long> {
}
