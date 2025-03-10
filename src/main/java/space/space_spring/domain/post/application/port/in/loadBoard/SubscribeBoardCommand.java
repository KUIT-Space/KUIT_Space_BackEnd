package space.space_spring.domain.post.application.port.in.loadBoard;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscribeBoardCommand {

    private Long spaceMemberId;

    private Long boardId;

    private String tagName;

    @Builder
    public SubscribeBoardCommand(Long spaceMemberId, Long boardId, String tagName) {
        this.spaceMemberId = spaceMemberId;
        this.boardId = boardId;
        this.tagName = tagName;
    }
}
