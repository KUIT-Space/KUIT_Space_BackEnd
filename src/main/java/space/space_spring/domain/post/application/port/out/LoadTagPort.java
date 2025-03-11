package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Tag;

import java.util.List;

public interface LoadTagPort {

    Tag loadByBoardAndName(Board board, String name);

    List<Tag> loadTagsByBoardIds(List<Long> boardIds);

}
