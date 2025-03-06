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

    private boolean isAnonymous;

    private Comment(Long id, Long boardId, Long discordId, Long targetId, Long spaceMemberId, String content, boolean isAnonymous) {
        this.id = id;
        this.boardId = boardId;
        this.discordId = discordId;
        this.targetId = targetId;
        this.spaceMemberId = spaceMemberId;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public static Comment createForPost(Long id, Long boardId, Long discordId, Long targetId, Long spaceMemberId, String content) {
        return new Comment(id, boardId, discordId, targetId, spaceMemberId, content, false);
    }

    public static Comment createForQuestion(Long id, Long boardId, Long discordId, Long targetId, Long spaceMemberId, String content, boolean isAnonymous) {
        return new Comment(id, boardId, discordId, targetId, spaceMemberId, content, isAnonymous);
    }

    public static Comment withoutIdForPost(Long boardId, Long discordId, Long targetId, Long spaceMemberId, String content) {
        return new Comment(null, boardId, discordId, targetId, spaceMemberId, content, false);
    }

    public static Comment withoutIdForQuestion(Long boardId, Long discordId, Long targetId, Long spaceMemberId, String content, boolean isAnonymous) {
        return new Comment(null, boardId, discordId, targetId, spaceMemberId, content, isAnonymous);
    }
}
