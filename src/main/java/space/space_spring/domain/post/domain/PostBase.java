package space.space_spring.domain.post.domain;

import lombok.Getter;
import org.w3c.dom.Text;

@Getter
public class PostBase {

    private Long id;

    private Long discordId;

    private Board board;

    private Text content;

    private PostBase(Long id, Long discordId, Board board, Text content) {
        this.id = id;
        this.discordId = discordId;
        this.board = board;
        this.content = content;
    }

}
