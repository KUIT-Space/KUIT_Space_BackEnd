package space.space_spring.domain.post.adapter.out.persistence.comment;

import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.post.domain.Content;

@Component
public class CommentMapper {

    public PostCommentJpaEntity toJpaEntity(PostBaseJpaEntity postBaseJpaEntity, PostJpaEntity postJpaEntity, Comment comment) {
        return PostCommentJpaEntity.create(postBaseJpaEntity, postJpaEntity, comment.isAnonymous());
    }

    public Comment toDomainEntity(PostBaseJpaEntity postBaseJpaEntity, PostCommentJpaEntity postCommentJpaEntity) {
        return Comment.create(
                postBaseJpaEntity.getId(),
                postBaseJpaEntity.getBoard().getId(),
                postBaseJpaEntity.getDiscordId(),
                postCommentJpaEntity.getPost().getPostBase().getId(),
                postBaseJpaEntity.getSpaceMember().getId(),
                Content.of(postBaseJpaEntity.getContent()),
                postCommentJpaEntity.isAnonymous()
        );
    }
}
