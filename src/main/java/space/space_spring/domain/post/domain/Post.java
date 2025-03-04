package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Post {

    private Long id; // postBaseId

    private Long discordId;

    private Long boardId;

    private Long spaceMemberId;

    private String title;

    private Content content;

    private Post(Long id, Long discordId, Long boardId, Long spaceMemberId, String title, Content content) {
        this.id = id;
        this.discordId = discordId;
        this.boardId = boardId;
        this.spaceMemberId = spaceMemberId;
        this.title = title;
        this.content = content;
    }

    public static Post of(Long id, Long discordId, Long boardId, Long spaceMemberId, String title, Content content) {
        return new Post(id, discordId, boardId, spaceMemberId, title, content);
    }

    public static Post withoutId(Long discordId, Long boardId, Long spaceMemberId, String title, Content content) {
        return new Post(null, discordId, boardId, spaceMemberId, title, content);
    }
}
