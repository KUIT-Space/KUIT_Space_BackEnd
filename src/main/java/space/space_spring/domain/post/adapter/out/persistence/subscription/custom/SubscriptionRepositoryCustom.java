package space.space_spring.domain.post.adapter.out.persistence.subscription.custom;

import space.space_spring.domain.post.adapter.out.persistence.subscription.SubscriptionJpaEntity;

public interface SubscriptionRepositoryCustom {

    void updateActiveById(Long id);

    void softDeleteById(Long id);
}
