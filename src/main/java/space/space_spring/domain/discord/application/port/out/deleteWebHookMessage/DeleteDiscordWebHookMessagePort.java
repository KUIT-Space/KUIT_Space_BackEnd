package space.space_spring.domain.discord.application.port.out.deleteWebHookMessage;

public interface DeleteDiscordWebHookMessagePort {
    void delete(String webHook,Long guildDiscordId,Long channelDiscordId,Long messageId);
    void deleteInThread(String webHook,Long guildDiscordId, Long threadDiscordId,Long messageId);
}
