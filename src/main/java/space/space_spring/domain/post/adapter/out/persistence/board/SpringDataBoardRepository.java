package space.space_spring.domain.post.adapter.out.persistence.board;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.domain.BoardType;

import java.util.List;

public interface SpringDataBoardRepository extends JpaRepository<BoardJpaEntity, Long> {
    List<BoardJpaEntity> findByBoardType(BoardType boardType);
}
