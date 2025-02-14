package space.space_spring.domain.post.adapter.out.persistence.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.domain.Board;

import space.space_spring.domain.space.adapter.out.persistence.SpaceMapper;
import space.space_spring.domain.space.domain.SpaceJpaEntity;

@Component
@RequiredArgsConstructor
public class BoardMapper {

    private final SpaceMapper spaceMapper;

    // 도메인 -> JPA 엔티티
    public BoardJpaEntity toJpaEntity(SpaceJpaEntity space, Board domain) {
        return BoardJpaEntity.create(
                space,
                domain.getDiscordId(),
                domain.getBoardType(),
                domain.getBoardName(),
                domain.getWebhookUrl()
        );
    }

    // JPA 엔티티 -> 도메인
    public Board toDomainEntity(BoardJpaEntity jpaEntity) {
        return Board.of(
                jpaEntity.getId(),
                jpaEntity.getSpace().getId(),
                jpaEntity.getDiscordId(),
                jpaEntity.getBoardName(),
                jpaEntity.getBoardType(),
                jpaEntity.getWebhookUrl()
        );
    }
}
