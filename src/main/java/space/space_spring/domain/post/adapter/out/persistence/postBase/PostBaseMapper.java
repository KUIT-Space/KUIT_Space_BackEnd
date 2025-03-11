package space.space_spring.domain.post.adapter.out.persistence.postBase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseInfo;

@Component
@RequiredArgsConstructor
public class PostBaseMapper {

    public PostBaseJpaEntity toJpaEntity(SpaceMemberJpaEntity spaceMember, BoardJpaEntity board, Post domain) {
        return PostBaseJpaEntity.create(
                domain.getDiscordId(),
                board,
                spaceMember,
                domain.getContent().getValue(),
                domain.getBaseInfo().getCreatedAt(),
                domain.getBaseInfo().getLastModifiedAt(),
                domain.getBaseInfo().getStatus()
        );
    }

    public PostBaseJpaEntity toJpaEntity(SpaceMemberJpaEntity spaceMember, BoardJpaEntity board, Comment domain) {
        return PostBaseJpaEntity.create(
                domain.getDiscordId(),
                board,
                spaceMember,
                domain.getContent(),
                domain.getBaseInfo().getCreatedAt(),
                domain.getBaseInfo().getLastModifiedAt(),
                domain.getBaseInfo().getStatus()
        );
    }
}
