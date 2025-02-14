package space.space_spring.domain.post.adapter.out.persistence.postBase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.domain.PostBase;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

@Component
@RequiredArgsConstructor
public class PostBaseMapper {

    PostBaseJpaEntity toJpaEntity(SpaceMemberJpaEntity spaceMember, BoardJpaEntity board, PostBase domain) {
        return PostBaseJpaEntity.create(
                domain.getDiscordId(),
                board,
                spaceMember,
                domain.getContent().getValue()
        );
    }
}
