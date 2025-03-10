package space.space_spring.domain.post.application.service;

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
    public void changeSubscription(SubscribeBoardCommand subscribeBoardCommand) {
        Long spaceMemberId = subscribeBoardCommand.getSpaceMemberId();
        Long boardId = subscribeBoardCommand.getBoardId();

        Board board = loadBoardPort.loadById(boardId);
        Tag tag = loadTagPort.loadByBoardAndName(board, subscribeBoardCommand.getTagName());

        Optional<Subscription> savedSubscription = loadSubscriptionPort.loadByInfos(spaceMemberId, boardId, tag.getId());

        if (savedSubscription.isPresent()) {
            // ACTIVE면 INACTIVE로, INACTIVE면 ACTIVE로
            if (savedSubscription.get().isActive()) {
                updateSubscriptionPort.inactivate(savedSubscription.get());
                return;
            }
            updateSubscriptionPort.activate(savedSubscription.get());
            return;
        }

        // 구독 내역이 없다면 생성
        Subscription subscription = Subscription.withoutId(spaceMemberId, boardId, tag.getId(), BaseInfo.ofEmpty());
        createSubscriptionPort.createSubscription(subscription);
    }
}
