package space.space_spring.domain.post.domain;

import lombok.Getter;
import space.space_spring.global.common.entity.BaseInfo;

@Getter
public class Like {

    private Long id;

    private Long targetId;          // 좋아요 표시의 target(= 게시글, 댓글)

    private Long spaceMemberId;

    private Boolean isLiked;

    private BaseInfo baseInfo;

    private Like(Long id, Long targetId, Long spaceMemberId, Boolean isLiked, BaseInfo baseInfo) {
        this.id = id;
        this.targetId = targetId;
        this.spaceMemberId = spaceMemberId;
        this.isLiked = isLiked;
        this.baseInfo = baseInfo;
    }

    public static Like of(Long id, Long targetId, Long spaceMemberId, Boolean isLiked, BaseInfo baseInfo) {
        return new Like(id, targetId, spaceMemberId, isLiked, baseInfo);
    }

    public static Like withoutId(Long targetId, Long spaceMemberId, Boolean isLiked) {
        return new Like(null, targetId, spaceMemberId, isLiked, BaseInfo.ofEmpty());
    }
}
