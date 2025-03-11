package space.space_spring.domain.post.adapter.out.persistence.subscription;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.subscription.custom.SubscriptionRepositoryCustom;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

public interface SubscriptionRepository extends JpaRepository<SubscriptionJpaEntity, Long>, SubscriptionRepositoryCustom {
    Optional<SubscriptionJpaEntity> findBySpaceMemberAndBoardIdAndTagId(SpaceMemberJpaEntity spaceMember, Long boardId, Long tagId);
}
