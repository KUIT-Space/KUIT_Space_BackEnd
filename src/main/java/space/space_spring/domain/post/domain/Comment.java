package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Comment {

    private Long id; // postBaseId

    private Long boardId;

    private Long discordId;

    private Long targetId; // post의 postBaseId

    private Long commentCreatorId;

    private Content content;

    private boolean isAnonymous;

    private Comment(Long id, Long boardId, Long discordId, Long targetId, Long commentCreatorId, Content content, boolean isAnonymous) {
        this.id = id;
        this.boardId = boardId;
        this.discordId = discordId;
        this.targetId = targetId;
        this.commentCreatorId = commentCreatorId;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public static Comment create(Long id, Long boardId, Long discordId, Long targetId, Long commentCreatorId, Content content, boolean isAnonymous) {
        return new Comment(id, boardId, discordId, targetId, commentCreatorId, content, isAnonymous);
    }

    public static Comment withoutId(Long boardId, Long discordId, Long targetId, Long commentCreatorId, Content content, boolean isAnonymous) {
        return new Comment(null, boardId, discordId, targetId, commentCreatorId, content, isAnonymous);
    }

    public boolean isCommentCreator(Long spaceMemberId) {
        return commentCreatorId.equals(spaceMemberId);
    }

    public void changeContent(Content content) {
        this.content = content;
    }

    public void changeAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
}
