package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Question {

    private Long id; // postBaseId

    private Long discordId;

    private Long boardId;

    private Long spaceMemberId;

    private boolean isAnonymous;

    private String title;

    private Content content;

    private Question(Long id, Long discordId, Long boardId, Long spaceMemberId, boolean isAnonymous, String title, Content content) {
        this.id = id;
        this.discordId = discordId;
        this.boardId = boardId;
        this.spaceMemberId = spaceMemberId;
        this.isAnonymous = isAnonymous;
        this.title = title;
        this.content = content;
    }

    public static Question of(Long id, Long discordId, Long boardId, Long spaceMemberId, boolean isAnonymous, String title, Content content) {
        return new Question(id, discordId, boardId, spaceMemberId, isAnonymous, title, content);
    }

    public static Question withoutId(Long discordId, Long boardId, Long spaceMemberId, boolean isAnonymous, String title, Content content) {
        return new Question(null, discordId, boardId, spaceMemberId, isAnonymous, title, content);
    }
}
