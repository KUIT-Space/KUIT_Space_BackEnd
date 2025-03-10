package space.space_spring.domain.post.application.port.in.subscribeBoard;

import space.space_spring.domain.post.application.port.in.loadBoard.SubscribeBoardCommand;

public interface SubscribeBoardUseCase {

    void subscribeBoard(SubscribeBoardCommand subscribeBoardCommand);

    void unsubscribeBoard(SubscribeBoardCommand subscribeBoardCommand);

}
