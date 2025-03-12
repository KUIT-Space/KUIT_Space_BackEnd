package space.space_spring.domain.post.application.port.in.createBoard;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.discord.domain.DiscordTags;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.post.domain.Tag;

import java.util.List;

@Getter
public class CreateBoardCommand {

    private Long spaceId;

    private Long discordId;

    private BoardType boardType;

    private String boardName;

    private String webhookUrl;

    private DiscordTags tags;

    @Builder
    private CreateBoardCommand (Long spaceId, Long discordId, BoardType boardType, String boardName, String webhookUrl,DiscordTags tags) {
        this.spaceId = spaceId;
        this.discordId = discordId;
        this.boardType = boardType;
        this.boardName = boardName;
        this.webhookUrl = webhookUrl;
        this.tags = tags;
    }
}
