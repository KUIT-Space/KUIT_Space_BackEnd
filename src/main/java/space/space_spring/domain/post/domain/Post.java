package space.space_spring.domain.post.domain;

import lombok.Getter;
import space.space_spring.global.common.entity.BaseInfo;

@Getter
public class Post {

    private Long id; // postBaseId

    private Long discordId;

    private Long boardId;

    private Long postCreatorId;

    private String title;

    private Content content;

    private BaseInfo baseInfo;

    private Boolean isAnonymous;

    private Post(Long id, Long discordId, Long boardId, Long postCreatorId, String title, Content content, BaseInfo baseInfo, Boolean isAnonymous) {
        this.id = id;
        this.discordId = discordId;
        this.boardId = boardId;
        this.postCreatorId = postCreatorId;
        this.title = title;
        this.content = content;
        this.baseInfo = baseInfo;
        this.isAnonymous = isAnonymous;
    }

    public static Post of(Long id, Long discordId, Long boardId, Long postCreatorId, String title, Content content, BaseInfo baseInfo, Boolean isAnonymous) {
        return new Post(id, discordId, boardId, postCreatorId, title, content, baseInfo, isAnonymous);
    }

    public static Post withoutId(Long discordId, Long boardId, Long postCreatorId, String title, Content content, BaseInfo baseInfo, Boolean isAnonymous) {
        return new Post(null, discordId, boardId, postCreatorId, title, content, baseInfo, isAnonymous);
    }

    public boolean isInBoard(Long boardId) {
        return this.boardId.equals(boardId);
    }

    public boolean isPostCreator(Long spaceMemberId) {
        return this.postCreatorId.equals(spaceMemberId);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(Content content) {
        this.content = content;
    }
}
