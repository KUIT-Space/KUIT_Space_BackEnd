package space.space_spring.domain.discord.application.port.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateDiscordThreadCommand {

    private String WebHookUrl;
    private Long channelDiscordId;

    private String startMessage;
    private String contentMessage;
    private String threadName;

    private String userName;
    private String avatarUrl;

    public CreateDiscordMessageCommand getStartMessage(){
        return CreateDiscordMessageCommand.builder()
                .name(this.userName)
                .Content(this.startMessage)
                .WebHookUrl(WebHookUrl)
                .avatarUrl(this.avatarUrl)
                .channelDiscordId(this.channelDiscordId)
                .build();
    }
}
