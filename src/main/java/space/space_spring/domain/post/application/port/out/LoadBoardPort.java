package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Board;

public interface LoadBoardPort {
    Board loadById(Long id);
}
