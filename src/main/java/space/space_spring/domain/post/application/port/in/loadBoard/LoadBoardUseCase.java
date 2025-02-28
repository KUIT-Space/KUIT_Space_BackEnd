package space.space_spring.domain.post.application.port.in.loadBoard;

import space.space_spring.domain.post.domain.Board;

public interface LoadBoardUseCase {
    Board findById(Long boardId);
}
