package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Subscription;

public interface UpdateSubscriptionPort {

    void activate(Subscription subscription);

    void inactivate(Subscription subscription);

}
