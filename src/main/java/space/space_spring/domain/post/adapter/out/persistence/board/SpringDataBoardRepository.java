package space.space_spring.domain.post.adapter.out.persistence.board;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;

import space.space_spring.domain.post.domain.BoardType;

import java.util.List;

import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.Optional;




public interface SpringDataBoardRepository extends JpaRepository<BoardJpaEntity, Long> {

    Optional<BoardJpaEntity> findByIdAndStatus(Long id, BaseStatusType baseStatusType);

    List<BoardJpaEntity> findByBoardTypeAndStatus(BoardType boardType, BaseStatusType status);

    List<BoardJpaEntity> findBySpaceIdAndStatus(Long spaceId, BaseStatusType status);

  Optional<BoardJpaEntity> findByDiscordIdAndStatus(Long discordId, BaseStatusType baseStatusType);

}
