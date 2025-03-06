package space.space_spring.domain.post.adapter.out.persistence.comment;

import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;

@Component
public class CommentMapper {

    /**
     * Comment 테이블 하나로 합치면 수정해야함 -> 익명 여부 추가
     */
    public PostCommentJpaEntity toJpaEntity(PostBaseJpaEntity postBaseJpaEntity, PostJpaEntity postJpaEntity) {
        return PostCommentJpaEntity.create(postBaseJpaEntity, postJpaEntity);
    }

}
