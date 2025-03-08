package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Tag {

    private Long id;

    private String tagName;

    private Long boardId;

    private Tag(Long id, String tagName, Long boardId) {
        this.id = id;
        this.tagName = tagName;
        this.boardId = boardId;
    }
}
