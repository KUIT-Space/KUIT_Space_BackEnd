package space.space_spring.domain.post.application.port.in.loadBoard;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscribeBoardCommand {

    private Long spaceMemberId;

    private Long boardId;

    private Long tagId;

    @Builder
    public SubscribeBoardCommand(Long spaceMemberId, Long boardId, Long tagId) {
        this.spaceMemberId = spaceMemberId;
        this.boardId = boardId;
        this.tagId = tagId;
    }
}
