package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Tag;

import java.util.List;

public interface LoadTagPort {

    Tag loadByIdAndBoard(Long tagId, Long boardId);

    List<Tag> loadTagsByBoardIds(List<Long> boardIds);

    List<Tag> loadByDiscordId(List<Long> discordIdOfTag);
}
