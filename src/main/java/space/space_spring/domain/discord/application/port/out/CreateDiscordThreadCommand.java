package space.space_spring.domain.discord.application.port.out;

import lombok.Builder;
import lombok.Getter;
import org.w3c.dom.Text;

@Getter
@Builder
public class CreateDiscordThreadCommand {

    private String webHookUrl;
    private Long channelDiscordId;
    private Long guildDiscordId;
    private String startMessage;
    private String contentMessage;
    private String threadName;

    private String userName;
    private String avatarUrl;

    public CreateDiscordMessageCommand getStartMessage(){
        return CreateDiscordMessageCommand.builder()
                .name(this.userName)
                .Content(this.startMessage)
                .webHookUrl(this.webHookUrl)
                .avatarUrl(this.avatarUrl)
                .guildDiscordId(this.guildDiscordId)
                .channelDiscordId(this.channelDiscordId)
                .build();
    }
}
