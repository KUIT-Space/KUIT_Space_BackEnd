package space.space_spring.domain.discord.application.port.out;

public interface CheckDiscordChannelPort {
    boolean isTextChannel(Long discordChannelId);
    boolean isForumChannel(Long discordChannelId);
    Long getRootChannelId(Long discordChannelId);
    String getChannelType(Long discordGuildId,Long discordChannelId);

    boolean isThreadExist(Long discordGuildId, Long discordChannelId);
}
