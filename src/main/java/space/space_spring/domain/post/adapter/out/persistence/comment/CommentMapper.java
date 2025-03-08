package space.space_spring.domain.post.adapter.out.persistence.comment;

import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.domain.Comment;

@Component
public class CommentMapper {

    public PostCommentJpaEntity toJpaEntity(PostBaseJpaEntity postBaseJpaEntity, PostJpaEntity postJpaEntity, Comment comment) {
        return PostCommentJpaEntity.create(postBaseJpaEntity, postJpaEntity, comment.isAnonymous());
    }

}
