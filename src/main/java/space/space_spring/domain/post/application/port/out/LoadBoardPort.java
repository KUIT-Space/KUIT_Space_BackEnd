package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Board;

import java.util.Optional;

public interface LoadBoardPort {
    Optional<Board> load(Long boardId);
}
