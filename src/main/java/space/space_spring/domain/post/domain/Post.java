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

    private Boolean isAnonymous;

    private Post(Long id, Long discordId, Long boardId, Long spaceMemberId, String title, Content content, BaseInfo baseInfo, Boolean isAnonymous) {
        this.id = id;
        this.discordId = discordId;
        this.boardId = boardId;
        this.spaceMemberId = spaceMemberId;
        this.title = title;
        this.content = content;
        this.baseInfo = baseInfo;
        this.isAnonymous = isAnonymous;
    }

    public static Post of(Long id, Long discordId, Long boardId, Long spaceMemberId, String title, Content content, BaseInfo baseInfo, Boolean isAnonymous) {
        return new Post(id, discordId, boardId, spaceMemberId, title, content, baseInfo, isAnonymous);
    }

    public static Post withoutId(Long discordId, Long boardId, Long spaceMemberId, String title, Content content, Boolean isAnonymous) {
        return new Post(null, discordId, boardId, spaceMemberId, title, content, BaseInfo.ofEmpty(), isAnonymous);
    }
}
