package space.space_spring.domain.post.adapter.out.persistence.subscription.custom;

import space.space_spring.domain.post.adapter.out.persistence.subscription.SubscriptionJpaEntity;

public interface SubscriptionRepositoryCustom {

    void updateActive(SubscriptionJpaEntity jpaEntity);

    void softDelete(SubscriptionJpaEntity jpaEntity);

}
