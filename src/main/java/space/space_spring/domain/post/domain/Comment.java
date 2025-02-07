package space.space_spring.domain.post.domain;

import lombok.Getter;
import org.w3c.dom.Text;

@Getter
public class Comment {

    private Long id;

    private PostBase postBaseId;

    public Comment(Long id, Long discordId, Board board, Text content) {

    }
}
