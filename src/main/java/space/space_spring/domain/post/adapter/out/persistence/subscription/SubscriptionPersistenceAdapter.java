package space.space_spring.domain.post.adapter.out.persistence.subscription;

import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_MEMBER_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TAG_NOT_FOUND;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.board.SpringDataBoardRepository;
import space.space_spring.domain.post.adapter.out.persistence.tag.TagJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.tag.SpringDataTagRepository;
import space.space_spring.domain.post.application.port.out.CreateSubscriptionPort;
import space.space_spring.domain.post.application.port.out.LoadSubscriptionPort;
import space.space_spring.domain.post.application.port.out.UpdateSubscriptionPort;
import space.space_spring.domain.post.domain.Subscription;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.exception.CustomException;

@Repository
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements LoadSubscriptionPort, CreateSubscriptionPort, UpdateSubscriptionPort {

    private final SpaceMemberRepository spaceMemberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SpringDataBoardRepository boardRepository;
    private final SpringDataTagRepository springDataTagRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public Optional<Subscription> loadByInfos(Long spaceMemberId, Long boardId, Long tagId) {
        SpaceMemberJpaEntity spaceMember = spaceMemberRepository.findByIdAndStatus(spaceMemberId, ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        return subscriptionRepository.findBySpaceMemberAndBoardIdAndTagId(spaceMember, boardId, tagId)
                .filter(subscription -> subscription.getStatus() == ACTIVE)
                .map(subscriptionMapper::toDomainEntity);
    }

    @Override
    public List<Long> loadSubscribedBoardIds(Long spaceMemberId) {
        return subscriptionRepository.findSubscribedBoardIdsBySpaceMemberId(spaceMemberId);
    }

    @Override
    public List<Subscription> loadBySpaceMember(Long spaceMemberId) {
        SpaceMemberJpaEntity spaceMember = spaceMemberRepository.findByIdAndStatus(spaceMemberId, ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        return subscriptionRepository.findBySpaceMemberAndStatus(spaceMember, ACTIVE).stream().map(subscriptionMapper::toDomainEntity).toList();
    }

    @Override
    public Optional<Subscription> loadByBoardId(Long boardId) {
        return subscriptionRepository.findByBoardIdAndStatus(boardId, ACTIVE).map(subscriptionMapper::toDomainEntity);
    }

    @Override
    public List<Map.Entry<Long, Long>> loadSubscribedBoardTagPairs(Long spaceMemberId) {
        List<Object[]> results = subscriptionRepository.findSubscribedBoardTagPairs(spaceMemberId);

        return results.stream()
                .map(result -> new AbstractMap.SimpleEntry<>(
                        (Long) result[0],
                        result[1] != null ? (Long) result[1] : null
                )).collect(Collectors.toList());
    }

    @Override
    public Subscription createSubscription(Subscription subscription) {
        SpaceMemberJpaEntity spaceMember = spaceMemberRepository.findByIdAndStatus(subscription.getSpaceMemberId(), ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));
        BoardJpaEntity boardJpaEntity = boardRepository.findByIdAndStatus(subscription.getBoardId(), ACTIVE)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        // tag가 없는 경우
        if (subscription.getTagId() == null) {
            SubscriptionJpaEntity subscriptionJpaEntity = subscriptionMapper.toJpaEntity(subscription, boardJpaEntity, null, spaceMember);
            SubscriptionJpaEntity saved = subscriptionRepository.save(subscriptionJpaEntity);

            return subscriptionMapper.toDomainEntity(saved);
        }

        // tag가 없는 경우
        TagJpaEntity tagJpaEntity = springDataTagRepository.findByIdAndStatus(subscription.getTagId(), ACTIVE)
                .orElseThrow(() -> new CustomException(TAG_NOT_FOUND));
        SubscriptionJpaEntity subscriptionJpaEntity = subscriptionMapper.toJpaEntity(subscription, boardJpaEntity, tagJpaEntity, spaceMember);
        SubscriptionJpaEntity saved = subscriptionRepository.save(subscriptionJpaEntity);

        return subscriptionMapper.toDomainEntity(saved);
    }

    @Override
    public void activate(Subscription subscription) {
        subscriptionRepository.updateActiveById(subscription.getId());
    }

    @Override
    public void inactivate(Subscription subscription) {
        subscriptionRepository.softDeleteById(subscription.getId());
    }
}
