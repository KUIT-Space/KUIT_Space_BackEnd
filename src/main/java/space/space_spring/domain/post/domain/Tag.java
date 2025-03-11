package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Tag {

    private Long id;

    private Long discordId;

    private String tagName;

    private Long boardId;

    private Tag(Long id, Long discordId, String tagName, Long boardId) {
        this.id = id;
        this.tagName = tagName;
        this.boardId = boardId;
    }

    public static Tag create(Long id, Long discordId, String tagName, Long boardId) {
        return new Tag(id, discordId, tagName, boardId);
    }
}
