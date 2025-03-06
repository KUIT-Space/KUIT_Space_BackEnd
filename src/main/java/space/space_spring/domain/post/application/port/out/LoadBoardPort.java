package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.BoardType;

import java.util.List;

public interface LoadBoardPort {
    Board loadById(Long id);
    List<Board> loadByType(BoardType type);
}
