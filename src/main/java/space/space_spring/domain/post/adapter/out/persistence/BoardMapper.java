package space.space_spring.domain.post.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.space.SpaceMapper;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.space.domain.SpaceJpaEntity;

@Component
@RequiredArgsConstructor
public class BoardMapper {

    private final SpaceMapper spaceMapper;

    // 도메인 -> JPA 엔티티
    public BoardJpaEntity toJpaEntity(SpaceJpaEntity space, Board domain) {
        return BoardJpaEntity.builder()
                .space(space)
                .discordId(domain.getDiscordId())
                .name(domain.getName())
                .boardType(domain.getBoardType())
                .webhookUrl(domain.getWebhookUrl())
                .build();
    }

    // JPA 엔티티 -> 도메인
    public Board toDomain(Space space, BoardJpaEntity jpaEntity) {
        return Board.of(
                jpaEntity.getId(),
                space,
                jpaEntity.getDiscordId(),
                jpaEntity.getName(),
                jpaEntity.getBoardType(),
                jpaEntity.getWebhookUrl()
        );
    }
}
