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

        Board board = loadBoardPort.loadById(boardId);
        Tag tag = loadTagPort.loadByBoardAndName(board, subscribeBoardCommand.getTagName());

        Optional<Subscription> savedSubscription = loadSubscriptionPort.loadByInfos(spaceMemberId, boardId, tag.getId());

        if (savedSubscription.isPresent()) {
            if (savedSubscription.get().isActive()) throw new CustomException(SUBSCRIPTION_ALREADY_EXIST);
            updateSubscriptionPort.activate(savedSubscription.get());
            return;
        }

        Subscription subscription = Subscription.withoutId(spaceMemberId, boardId, tag.getId(), BaseInfo.ofEmpty());
        createSubscriptionPort.createSubscription(subscription);
    }

    @Transactional
    @Override
    public void unsubscribeBoard(SubscribeBoardCommand subscribeBoardCommand) {
        Long spaceMemberId = subscribeBoardCommand.getSpaceMemberId();
        Long boardId = subscribeBoardCommand.getBoardId();

        Board board = loadBoardPort.loadById(boardId);
        Tag tag = loadTagPort.loadByBoardAndName(board, subscribeBoardCommand.getTagName());

        Optional<Subscription> savedSubscription = loadSubscriptionPort.loadByInfos(spaceMemberId, boardId, tag.getId());

        if (savedSubscription.isPresent()) {
            if (savedSubscription.get().isActive()) {
                updateSubscriptionPort.inactivate(savedSubscription.get());
                return;
            }
        }
        throw new CustomException(SUBSCRIPTION_NOT_EXIST);
    }
}
