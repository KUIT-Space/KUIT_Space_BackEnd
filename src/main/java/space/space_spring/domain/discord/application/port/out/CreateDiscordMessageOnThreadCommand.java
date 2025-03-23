package space.space_spring.domain.discord.application.port.out;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.discord.adapter.out.discord.WebHookMessage;

@Getter
@Builder
public class CreateDiscordMessageOnThreadCommand {

    private String webHookUrl;
    //thread id 혹은 시작 message id
    private Long threadChannelDiscordId;
    //thread를 달고 싶은 message의 postId
    private Long originPostId;
    //thread를 달고 싶은 message가 속한 원래 channel id. parentChannelId
    private String originPostTitle;

    private Long originChannelId;
    private Long guildDiscordId;

    private String content;

    private String userName;
    private String avatarUrl;

    public WebHookMessage getWebHookMessage(){
        return WebHookMessage.builder()
                .avatarUrl(this.avatarUrl)
                .userName(this.userName)
                .content(this.content)
                .WebHookUrl(this.webHookUrl)
                .build();
    }
}
