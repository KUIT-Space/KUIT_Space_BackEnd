package space.space_spring.domain.post.domain;

import lombok.Getter;
import space.space_spring.global.common.entity.BaseInfo;

@Getter
public class Post {

    private Long id; // postBaseId

    private Long discordId;

    private Long boardId;

    private Long spaceMemberId;

    private String title;

    private Content content;

    private BaseInfo baseInfo;

    private Post(Long id, Long discordId, Long boardId, Long spaceMemberId, String title, Content content, BaseInfo baseInfo) {
        this.id = id;
        this.discordId = discordId;
        this.boardId = boardId;
        this.spaceMemberId = spaceMemberId;
        this.title = title;
        this.content = content;
        this.baseInfo = baseInfo;
    }

    public static Post of(Long id, Long discordId, Long boardId, Long spaceMemberId, String title, Content content, BaseInfo baseInfo) {
        return new Post(id, discordId, boardId, spaceMemberId, title, content, baseInfo);
    }

    public static Post withoutId(Long discordId, Long boardId, Long spaceMemberId, String title, Content content) {
        return new Post(null, discordId, boardId, spaceMemberId, title, content, BaseInfo.ofEmpty());
    }

    public boolean isInBoard(Long boardId) {
        return this.boardId.equals(boardId);
    }
}
