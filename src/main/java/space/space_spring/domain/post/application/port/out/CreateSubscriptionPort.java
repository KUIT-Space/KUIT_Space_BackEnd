package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Subscription;

public interface CreateSubscriptionPort {

    Subscription createSubscription(Subscription subscription);

}
