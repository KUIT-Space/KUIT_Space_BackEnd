package space.space_spring.domain.discord.application.port.in.discord;

import space.space_spring.domain.spaceMember.application.port.out.GuildMember;

public interface CreateSpaceMemberInDiscordUseCase {
    void create(GuildMember guildMember,Long guildId);
}
