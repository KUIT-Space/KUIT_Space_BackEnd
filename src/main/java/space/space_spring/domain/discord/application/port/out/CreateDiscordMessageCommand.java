package space.space_spring.domain.discord.application.port.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateDiscordMessageCommand {

    private Long guildDiscordId;
    private Long channelDiscordId;
    private String name;
    private String avatarUrl;

    private String WebHookUrl;

    private String Content;
}
