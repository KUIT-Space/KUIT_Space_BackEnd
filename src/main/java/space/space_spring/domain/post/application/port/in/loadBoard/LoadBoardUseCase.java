package space.space_spring.domain.post.application.port.in.loadBoard;

import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.BoardType;

import java.util.Optional;

public interface LoadBoardUseCase {
    Board findById(Long boardId);
    Optional<BoardType> getBoardTypeById(Long boardId);
}
