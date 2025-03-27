package space.space_spring.domain.post.adapter.out.persistence.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.tag.TagJpaEntity;
import space.space_spring.domain.post.domain.Subscription;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseInfo;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {

    public SubscriptionJpaEntity toJpaEntity(Subscription subscription, BoardJpaEntity boardJpaEntity, TagJpaEntity tagJpaEntity, SpaceMemberJpaEntity spaceMemberJpaEntity) {
        return SubscriptionJpaEntity.create(
                spaceMemberJpaEntity,
                boardJpaEntity,
                tagJpaEntity,
                subscription.getBaseInfo().getCreatedAt(),
                subscription.getBaseInfo().getLastModifiedAt(),
                subscription.getBaseInfo().getStatus());
    }

    public Subscription toDomainEntity(SubscriptionJpaEntity subscriptionJpaEntity) {
        BaseInfo baseInfo = BaseInfo.of(subscriptionJpaEntity.getCreatedAt(), subscriptionJpaEntity.getLastModifiedAt(), subscriptionJpaEntity.getStatus());

        return Subscription.create(
                subscriptionJpaEntity.getId(),
                subscriptionJpaEntity.getSpaceMember().getId(),
                subscriptionJpaEntity.getBoard().getId(),
                subscriptionJpaEntity.getTag() != null ? subscriptionJpaEntity.getTag().getId() : null,
                baseInfo);
    }

}
