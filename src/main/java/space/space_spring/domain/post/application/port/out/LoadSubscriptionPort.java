package space.space_spring.domain.post.application.port.out;

import java.util.Optional;
import space.space_spring.domain.post.domain.Subscription;

public interface LoadSubscriptionPort {

    Optional<Subscription> loadByInfos(Long spaceMemberId, Long boardId, Long tagId);

}
