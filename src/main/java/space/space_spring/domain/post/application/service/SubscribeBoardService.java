package space.space_spring.domain.post.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.loadBoard.SubscribeBoardCommand;
import space.space_spring.domain.post.application.port.in.subscribeBoard.SubscribeBoardUseCase;
import space.space_spring.domain.post.application.port.out.CreateSubscriptionPort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.application.port.out.LoadSubscriptionPort;
import space.space_spring.domain.post.application.port.out.LoadTagPort;
import space.space_spring.domain.post.application.port.out.UpdateSubscriptionPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Subscription;
import space.space_spring.domain.post.domain.Tag;
import space.space_spring.global.common.entity.BaseInfo;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class SubscribeBoardService implements SubscribeBoardUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadTagPort loadTagPort;
    private final LoadSubscriptionPort loadSubscriptionPort;
    private final CreateSubscriptionPort createSubscriptionPort;
    private final UpdateSubscriptionPort updateSubscriptionPort;

    @Transactional
    @Override
    public void subscribeBoard(SubscribeBoardCommand subscribeBoardCommand) {
        Long spaceMemberId = subscribeBoardCommand.getSpaceMemberId();
        Long boardId = subscribeBoardCommand.getBoardId();

        Subscription newSubscription;

        if (subscribeBoardCommand.getTagId() == null) { // 태그가 없는 경우 -> 보드로만 구독 찾음
            Optional<Subscription> savedSubscription = loadSubscriptionPort.loadByBoardId(boardId);

            // 구독 이력이 있는 경우
            if (savedSubscription.isPresent()) {
                if (savedSubscription.get().isActive()) throw new CustomException(SUBSCRIPTION_ALREADY_EXIST); // 이미 구독한 경우

                updateSubscriptionPort.activate(savedSubscription.get()); // 구독 상태 활성화
                return;
            }
            
            // 구독 이력이 없는 경우
            newSubscription = Subscription.withoutIdAndTag(spaceMemberId, boardId, BaseInfo.ofEmpty());
        } else { // 태그가 있는 경우 -> 태그 + 보드로 구독 찾음
            Tag tag = loadTagPort.loadByIdAndBoard(subscribeBoardCommand.getTagId(), boardId);
            Optional<Subscription> savedSubscription = loadSubscriptionPort.loadByInfos(spaceMemberId, boardId, tag.getId());

            // 구독 이력이 있는 경우
            if (savedSubscription.isPresent()) {
                if (savedSubscription.get().isActive()) throw new CustomException(SUBSCRIPTION_ALREADY_EXIST);

                updateSubscriptionPort.activate(savedSubscription.get()); // 구독 상태 활성화
                return;
            }
            
            // 구독 이력이 없는 경우
            newSubscription = Subscription.withoutId(spaceMemberId, boardId, tag.getId(), BaseInfo.ofEmpty());
        }
        
        // 구독 이력이 없는 경우들에 대해 구독 생성
        createSubscriptionPort.createSubscription(newSubscription);
    }

    @Transactional
    @Override
    public void unsubscribeBoard(SubscribeBoardCommand subscribeBoardCommand) {
        Long spaceMemberId = subscribeBoardCommand.getSpaceMemberId();
        Long boardId = subscribeBoardCommand.getBoardId();

        Optional<Subscription> savedSubscription;
        if (subscribeBoardCommand.getTagId() == null) {
            // tag 없으면 -> 보드로만 구독 찾음
            savedSubscription = loadSubscriptionPort.loadByBoardId(boardId);
        } else {
            // tag 있으면 -> 태그 + 보드로 구독 찾음
            Tag tag = loadTagPort.loadByIdAndBoard(subscribeBoardCommand.getTagId(), boardId);
            savedSubscription = loadSubscriptionPort.loadByInfos(spaceMemberId, boardId, tag.getId());
        }

        if (savedSubscription.isPresent()) {
            if (savedSubscription.get().isActive()) {
                updateSubscriptionPort.inactivate(savedSubscription.get());
                return;
            }
        }
        throw new CustomException(SUBSCRIPTION_NOT_EXIST);
    }
}
