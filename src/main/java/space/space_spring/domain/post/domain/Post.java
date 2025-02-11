package space.space_spring.domain.post.domain;

import lombok.Getter;
import org.w3c.dom.Text;

@Getter
public class Post {

    private Long id;

    private PostBase postBaseId;

    private String title;

    public Post(Long id, Long discordId, Board board, Text content, String title) {

        this.title = title;

    }

}
