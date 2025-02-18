package space.space_spring.domain.post.application.port.in.createBoard;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.domain.BoardType;

@Getter
public class CreateBoardCommand {

    private Long spaceId;

    private Long discordId;

    private BoardType boardType;

    private String boardName;

    private String webhookUrl;

    @Builder
    private CreateBoardCommand (Long spaceId, Long discordId, BoardType boardType, String boardName, String webhookUrl) {
        this.spaceId = spaceId;
        this.discordId = discordId;
        this.boardType = boardType;
        this.boardName = boardName;
        this.webhookUrl = webhookUrl;
    }
}
