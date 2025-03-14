package space.space_spring.domain.discord.application.port.out;

import java.util.Optional;

public interface WebHookPort {

    String createSpaceWebHook(Long channelDiscordId);
    Optional<String> getSpaceWebHook(Long channelDiscordId);
    String getOrCreate(Long channelDiscordId);
}
