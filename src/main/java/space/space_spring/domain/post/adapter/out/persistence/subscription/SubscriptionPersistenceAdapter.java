package space.space_spring.domain.post.adapter.out.persistence.subscription;

import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_MEMBER_NOT_FOUND;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.application.port.out.CreateSubscriptionPort;
import space.space_spring.domain.post.application.port.out.LoadSubscriptionPort;
import space.space_spring.domain.post.application.port.out.UpdateSubscriptionPort;
import space.space_spring.domain.post.domain.Subscription;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.exception.CustomException;

@Repository
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements LoadSubscriptionPort, CreateSubscriptionPort, UpdateSubscriptionPort {

    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public Optional<Subscription> loadByInfos(Long spaceMemberId, Long boardId, Long tagId) {
        SpaceMemberJpaEntity spaceMember = spaceMemberRepository.findByIdAndStatus(spaceMemberId, ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        return subscriptionRepository.findBySpaceMemberAndBoardIdAndTagId(spaceMember, boardId, tagId).map(subscriptionMapper::toDomainEntity);
    }

    @Override
    public Subscription createSubscription(Subscription subscription) {
        SpaceMemberJpaEntity spaceMember = spaceMemberRepository.findByIdAndStatus(subscription.getSpaceMemberId(), ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        SubscriptionJpaEntity subscriptionJpaEntity = subscriptionMapper.toJpaEntity(subscription, spaceMember);
        SubscriptionJpaEntity saved = subscriptionRepository.save(subscriptionJpaEntity);

        return subscriptionMapper.toDomainEntity(saved);
    }

    @Override
    public void activate(Subscription subscription) {
        SpaceMemberJpaEntity spaceMember = spaceMemberRepository.findByIdAndStatus(subscription.getSpaceMemberId(), ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        subscriptionRepository.updateActive(subscriptionMapper.toJpaEntity(subscription, spaceMember));
    }

    @Override
    public void inactivate(Subscription subscription) {
        SpaceMemberJpaEntity spaceMember = spaceMemberRepository.findByIdAndStatus(subscription.getSpaceMemberId(), ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        subscriptionRepository.softDelete(subscriptionMapper.toJpaEntity(subscription, spaceMember));
    }
}
