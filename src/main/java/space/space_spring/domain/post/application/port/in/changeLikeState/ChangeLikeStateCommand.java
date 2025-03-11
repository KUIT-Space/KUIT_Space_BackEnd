package space.space_spring.domain.post.application.port.in.changeLikeState;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangeLikeStateCommand {

    private Long spaceId;

    private Long boardId;

    private Long targetId;

    private Long spaceMemberId;

    private boolean changeTo;

    @Builder
    public ChangeLikeStateCommand(Long spaceId, Long boardId, Long targetId, Long spaceMemberId, boolean changeTo) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.targetId = targetId;
        this.spaceMemberId = spaceMemberId;
        this.changeTo = changeTo;
    }
}
