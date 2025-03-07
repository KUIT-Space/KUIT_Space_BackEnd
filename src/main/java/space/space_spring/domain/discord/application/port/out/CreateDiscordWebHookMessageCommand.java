package space.space_spring.domain.discord.application.port.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateDiscordWebHookMessageCommand {

    private Long guildDiscordId;
    private Long channelDiscordId;
    private String name;
    private String avatarUrl;

    private String webHookUrl;
    private String title;
    private String content;


    //private List<Tag> tags;
}
