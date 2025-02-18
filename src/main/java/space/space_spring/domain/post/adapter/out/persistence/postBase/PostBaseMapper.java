package space.space_spring.domain.post.adapter.out.persistence.postBase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

@Component
@RequiredArgsConstructor
public class PostBaseMapper {

    public PostBaseJpaEntity toJpaEntity(Long discordId, SpaceMemberJpaEntity spaceMember, BoardJpaEntity board, Content content) {
        return PostBaseJpaEntity.create(
                discordId,
                board,
                spaceMember,
                content.getValue()
        );
    }
}
