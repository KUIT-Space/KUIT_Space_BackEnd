package space.space_spring.domain.post.adapter.out.persistence.subscription.custom;

import static space.space_spring.domain.post.adapter.out.persistence.subscription.QSubscriptionJpaEntity.subscriptionJpaEntity;
import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;
import static space.space_spring.global.common.enumStatus.BaseStatusType.INACTIVE;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.subscription.SubscriptionJpaEntity;

@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void updateActive(SubscriptionJpaEntity subscription) {
        jpaQueryFactory
                .update(subscriptionJpaEntity)
                .set(subscriptionJpaEntity.status, ACTIVE)
                .where(subscriptionJpaEntity.id.eq(subscription.getId()))
                .execute();
    }

    @Override
    public void softDelete(SubscriptionJpaEntity subscription) {
        jpaQueryFactory
                .update(subscriptionJpaEntity)
                .set(subscriptionJpaEntity.status, INACTIVE)
                .where(subscriptionJpaEntity.id.eq(subscription.getId()))
                .execute();
    }
}
