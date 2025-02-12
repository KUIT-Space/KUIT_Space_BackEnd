package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class PostBase {

    private Long id;

    private Long discordId;

    private Long boardId;

    private Long spaceMemberId;

    private Content content;

    private PostBase(Long id, Long discordId, Long boardId, Long spaceMemberId, Content content) {
        this.id = id;
        this.discordId = discordId;
        this.boardId = boardId;
        this.spaceMemberId = spaceMemberId;
        this.content = content;
    }

    public static PostBase of(Long id, Long discordId, Long boardId, Long spaceMemberId, Content content) {
        return new PostBase(id, discordId, boardId, spaceMemberId, content);
    }

    public static PostBase withoutId(Long discordId, Long boardId, Long spaceMemberId, Content content) {
        return new PostBase(null, discordId, boardId, spaceMemberId, content);
    }
}
