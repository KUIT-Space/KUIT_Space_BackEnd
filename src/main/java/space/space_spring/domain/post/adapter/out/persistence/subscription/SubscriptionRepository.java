package space.space_spring.domain.post.adapter.out.persistence.subscription;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.subscription.custom.SubscriptionRepositoryCustom;
import space.space_spring.domain.post.domain.Subscription;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

public interface SubscriptionRepository extends JpaRepository<SubscriptionJpaEntity, Long>, SubscriptionRepositoryCustom {
    Optional<SubscriptionJpaEntity> findBySpaceMemberAndBoardIdAndTagId(SpaceMemberJpaEntity spaceMember, Long boardId, Long tagId);

    Optional<SubscriptionJpaEntity> findByBoardIdAndStatus(Long boardId, BaseStatusType baseStatusType);

}
