package space.space_spring.domain.post.adapter.out.persistence.comment;

import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.global.common.entity.BaseInfo;

@Component
public class CommentMapper {

    public PostCommentJpaEntity toJpaEntity(PostBaseJpaEntity postBaseJpaEntity, PostJpaEntity postJpaEntity, Comment comment) {
        return PostCommentJpaEntity.create(postBaseJpaEntity, postJpaEntity, comment.isAnonymous());
    }

    public Comment toDomainEntity(PostCommentJpaEntity jpaEntity) {
        BaseInfo baseInfo = BaseInfo.of(
                jpaEntity.getPostBase().getCreatedAt(),
                jpaEntity.getPostBase().getLastModifiedAt(),
                jpaEntity.getPostBase().getStatus()
        );

        return Comment.create(
                jpaEntity.getId(),
                jpaEntity.getPostBase().getBoard().getId(),
                jpaEntity.getPostBase().getDiscordId(),
                jpaEntity.getPost().getId(),
                jpaEntity.getPostBase().getSpaceMember().getId(),
                Content.of(jpaEntity.getPostBase().getContent()),
                jpaEntity.isAnonymous(),
                baseInfo
        );
    }
}
