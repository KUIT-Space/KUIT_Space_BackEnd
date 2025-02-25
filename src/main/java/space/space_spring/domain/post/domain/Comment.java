package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Comment {

    private Long id; // postBaseId

    private Long boardId;

    private Long discordId;

    private Long targetId; // post, question의 postBaseId

    private Long spaceMemberId;

    private String content;

    private Comment(Long id, Long boardId, Long discordId, Long targetId, Long spaceMemberId, String content) {
        this.id = id;
        this.boardId = boardId;
        this.discordId = discordId;
        this.targetId = targetId;
        this.spaceMemberId = spaceMemberId;
        this.content = content;
    }

    public static Comment of(Long id, Long boardId, Long discordId, Long targetId, Long spaceMemberId, String content) {
        return new Comment(id, boardId, discordId, targetId, spaceMemberId, content);
    }

    public static Comment withoutId(Long boardId, Long discordId, Long targetId, Long spaceMemberId, String content) {
        return new Comment(null, boardId, discordId, targetId, spaceMemberId, content);
    }
}
