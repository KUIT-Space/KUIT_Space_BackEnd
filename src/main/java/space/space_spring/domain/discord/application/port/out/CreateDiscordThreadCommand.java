package space.space_spring.domain.discord.application.port.out;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.discord.adapter.out.discord.WebHookMessage;

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
    //private List<Tag> tags;

    public CreateDiscordWebHookMessageCommand getStartMessage(){
        return CreateDiscordWebHookMessageCommand.builder()
                .name(this.userName)
                .content(this.startMessage)
                .webHookUrl(this.webHookUrl)
                .avatarUrl(this.avatarUrl)
                .guildDiscordId(this.guildDiscordId)
                .channelDiscordId(this.channelDiscordId)
                .build();
    }
    public WebHookMessage getContentWebHookMessage(){
        return WebHookMessage.builder()
                .WebHookUrl(this.webHookUrl)
                .avatarUrl(this.avatarUrl)
                .content(this.contentMessage)
                .userName(this.userName)
                .build();
    }
}
