package space.space_spring.domain.discord.application.port.out;

public interface CreateDiscordRolePort {

    boolean createRole(Long discordGuildId,String name,Integer rgb);
}
