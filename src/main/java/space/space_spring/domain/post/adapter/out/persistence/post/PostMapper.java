package space.space_spring.domain.post.adapter.out.persistence.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.domain.post.domain.Post;

@Component
@RequiredArgsConstructor
public class PostMapper {

    PostJpaEntity toJpaEntity(PostBaseJpaEntity postBase, Post domain) {
        return PostJpaEntity.create(
                postBase,
                domain.getTitle()
        );
    }

    public Post toDomainEntity(PostJpaEntity jpaEntity) {
        return Post.of(
                jpaEntity.getPostBase().getId(),
                jpaEntity.getPostBase().getDiscordId(),
                jpaEntity.getPostBase().getBoard().getId(),
                jpaEntity.getPostBase().getSpaceMember().getId(),
                jpaEntity.getTitle(),
                Content.of(jpaEntity.getPostBase().getContent())
        );
    }
}
