package space.space_spring.domain.post.application.port.in.readPostDetail;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReadPostDetailCommand {

    private Long spaceId;

    private Long boardId;

    private Long postId;

    private Long spaceMemberId;

    @Builder
    public ReadPostDetailCommand(Long spaceId, Long boardId, Long postId, Long spaceMemberId) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postId = postId;
        this.spaceMemberId = spaceMemberId;
    }
}
