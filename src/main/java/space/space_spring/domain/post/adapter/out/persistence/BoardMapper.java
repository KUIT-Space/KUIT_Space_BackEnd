package space.space_spring.domain.post.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.space.SpaceMapper;
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
    public Board toDomain(Long spaceId, BoardJpaEntity jpaEntity) {
        return Board.of(
                jpaEntity.getId(),
                spaceId,
                jpaEntity.getDiscordId(),
                jpaEntity.getBoardName(),
                jpaEntity.getBoardType(),
                jpaEntity.getWebhookUrl()
        );
    }
}
