package space.space_spring.domain.post.domain;

import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.global.common.entity.BaseInfo;

@Getter
public class Subscription {

    private Long id;

    private Long spaceMemberId;

    private Long boardId;

    private Long tagId;

    private BaseInfo baseInfo;

    @Builder
    private Subscription(Long id, Long spaceMemberId, Long boardId, Long tagId, BaseInfo baseInfo) {
        this.id = id;
        this.spaceMemberId = spaceMemberId;
        this.boardId = boardId;
        this.tagId = tagId;
        this.baseInfo = baseInfo;
    }

    public static Subscription create(Long id, Long spaceMemberId, Long boardId, Long tagId, BaseInfo baseInfo) {
        return Subscription.builder()
                .id(id)
                .spaceMemberId(spaceMemberId)
                .boardId(boardId)
                .tagId(tagId)
                .baseInfo(baseInfo)
                .build();
    }

    public static Subscription withoutId(Long spaceMemberId, Long boardId, Long tagId, BaseInfo baseInfo) {
        return Subscription.builder()
                .spaceMemberId(spaceMemberId)
                .boardId(boardId)
                .tagId(tagId)
                .baseInfo(baseInfo)
                .build();
    }

    public static Subscription withoutIdAndTag(Long spaceMemberId, Long boardId, BaseInfo baseInfo) {
        return Subscription.builder()
                .spaceMemberId(spaceMemberId)
                .boardId(boardId)
                .baseInfo(baseInfo)
                .build();
    }

    public boolean isActive() {
        return this.baseInfo.getStatus().equals(ACTIVE);
    }
}
