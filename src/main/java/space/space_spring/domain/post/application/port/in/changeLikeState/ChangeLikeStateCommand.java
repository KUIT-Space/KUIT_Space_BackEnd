package space.space_spring.domain.post.application.port.in.changeLikeState;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangeLikeStateCommand {

    private Long spaceId;

    private Long boardId;

    private Long targetId;

    private Long spaceMemberId;

    @Builder
    public ChangeLikeStateCommand(Long spaceId, Long boardId, Long targetId, Long spaceMemberId) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.targetId = targetId;
        this.spaceMemberId = spaceMemberId;
    }
}
