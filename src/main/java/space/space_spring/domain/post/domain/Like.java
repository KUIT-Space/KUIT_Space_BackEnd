package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Like {

    private Long id;

    private Long targetId;          // 좋아요 표시의 target(= 게시글, 댓글)

    private Long spaceMemberId;

    private boolean isLiked;

    private Like(Long id, Long targetId, Long spaceMemberId, boolean isLiked) {
        this.id = id;
        this.targetId = targetId;
        this.spaceMemberId = spaceMemberId;
        this.isLiked = isLiked;
    }

    public static Like of(Long id, Long targetId, Long spaceMemberId, boolean isLiked) {
        return new Like(id, targetId, spaceMemberId, isLiked);
    }

    public static Like withoutId(Long targetId, Long spaceMemberId, boolean isLiked) {
        return new Like(null, targetId, spaceMemberId, isLiked);
    }
}
