package space.space_spring.domain.post.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Subscription {

    private Long id;

    private Long spaceMemberId;

    private Long boardId;

    private Long tagId;

    @Builder
    private Subscription(Long id, Long spaceMemberId, Long boardId, Long tagId) {
        this.id = id;
        this.spaceMemberId = spaceMemberId;
        this.boardId = boardId;
        this.tagId = tagId;
    }

    public static Subscription create(Long id, Long spaceMemberId, Long boardId, Long tagId) {
        return Subscription.builder()
                .id(id)
                .spaceMemberId(spaceMemberId)
                .boardId(boardId)
                .tagId(tagId)
                .build();
    }

    public static Subscription withoutId(Long spaceMemberId, Long boardId, Long tagId) {
        return Subscription.builder()
                .spaceMemberId(spaceMemberId)
                .boardId(boardId)
                .tagId(tagId)
                .build();
    }

}
