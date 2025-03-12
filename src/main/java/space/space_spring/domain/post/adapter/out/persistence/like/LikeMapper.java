package space.space_spring.domain.post.adapter.out.persistence.like;

import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.domain.Like;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseInfo;

@Component
public class LikeMapper {

    public Like toDomainEntity(LikeJpaEntity jpaEntity) {
        BaseInfo baseInfo = BaseInfo.of(
                jpaEntity.getCreatedAt(),
                jpaEntity.getLastModifiedAt(),
                jpaEntity.getStatus()
        );

        return Like.of(
                jpaEntity.getId(),
                jpaEntity.getPostBase().getId(),
                jpaEntity.getSpaceMember().getId(),
                jpaEntity.getIsLiked(),
                baseInfo
        );
    }

    public LikeJpaEntity toJpaEntity(PostBaseJpaEntity postBaseJpaEntity, SpaceMemberJpaEntity spaceMemberJpaEntity, Like domain) {
        return LikeJpaEntity.create(
                postBaseJpaEntity,
                spaceMemberJpaEntity,
                domain.getIsLiked()
        );
    }
}
