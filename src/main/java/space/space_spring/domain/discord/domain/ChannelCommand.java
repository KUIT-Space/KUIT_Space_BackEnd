package space.space_spring.domain.discord.domain;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardCommand;
import space.space_spring.domain.post.domain.BoardType;

import java.util.List;

@Builder
@Getter
public class ChannelCommand {
    private String channelName;
    private Long channelDiscordId;
    private Long spaceId;
    private String webhookUrl;
    private DiscordTags tags;

    public CreateBoardCommand getCreateBoardCommand(BoardType boardType){
        return CreateBoardCommand.builder()
                .boardName(this.channelName)
                .webhookUrl(this.webhookUrl)
                .spaceId(this.spaceId)
                .discordId(this.channelDiscordId)
                .boardType(boardType)
                .tags(this.tags)
                .build();
    }
}
