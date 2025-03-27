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
    public void updateActiveById(Long id) {
        jpaQueryFactory
                .update(subscriptionJpaEntity)
                .set(subscriptionJpaEntity.status, ACTIVE)
                .where(subscriptionJpaEntity.id.eq(id))
                .execute();
    }

    @Override
    public void softDeleteById(Long id) {
        jpaQueryFactory
                .update(subscriptionJpaEntity)
                .set(subscriptionJpaEntity.status, INACTIVE)
                .where(subscriptionJpaEntity.id.eq(id))
                .execute();
    }
}
