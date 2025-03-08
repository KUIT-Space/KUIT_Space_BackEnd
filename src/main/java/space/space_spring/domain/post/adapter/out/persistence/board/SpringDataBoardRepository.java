package space.space_spring.domain.post.adapter.out.persistence.board;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.Optional;

public interface SpringDataBoardRepository extends JpaRepository<BoardJpaEntity, Long> {

    Optional<BoardJpaEntity> findByIdAndStatus(Long id, BaseStatusType baseStatusType);
}
