package space.space_spring.domain.post.adapter.out.persistence.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.global.common.entity.BaseInfo;

@Component
@RequiredArgsConstructor
public class PostMapper {

    PostJpaEntity toJpaEntity(PostBaseJpaEntity postBase, Post domain) {
        return PostJpaEntity.create(
                postBase,
                domain.getTitle(),
                domain.getIsAnonymous()
        );
    }

    public Post toDomainEntity(PostJpaEntity jpaEntity) {
        PostBaseJpaEntity postBase = jpaEntity.getPostBase();

        // BaseInfo를 BaseJpaEntity에서 가져와서 매핑
        BaseInfo baseInfo = BaseInfo.of(
                postBase.getCreatedAt(),
                postBase.getLastModifiedAt(),
                postBase.getStatus()
        );

        return Post.of(
                postBase.getId(),
                postBase.getDiscordId(),
                postBase.getBoard().getId(),
                postBase.getSpaceMember().getId(),
                jpaEntity.getTitle(),
                Content.of(postBase.getContent()),
                baseInfo,
                jpaEntity.getIsAnonymous()

        );
    }
}
