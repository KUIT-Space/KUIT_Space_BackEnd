package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Tag;

public interface LoadTagPort {

    Tag loadByBoardAndName(Board board, String name);

}
