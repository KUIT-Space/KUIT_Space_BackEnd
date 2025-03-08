package space.space_spring.domain.discord.adapter.out.discord;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WebHookMessage {
    private String avatarUrl;
    private String userName;
    private String content;
    private String WebHookUrl;
}
