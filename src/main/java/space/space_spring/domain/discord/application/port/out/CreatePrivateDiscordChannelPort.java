package space.space_spring.domain.discord.application.port.out;

public interface CreatePrivateDiscordChannelPort {
    Long createPrivateChannel(Long guildId,Long userId);
}
