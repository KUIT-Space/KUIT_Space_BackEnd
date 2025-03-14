package space.space_spring.domain.discord.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateBoardInDiscordCommand {

    private Long channelDiscordId;
    private Long guildDiscordId;
    private String name;
    private String WebHookUrl;
    private String boardType;



}
