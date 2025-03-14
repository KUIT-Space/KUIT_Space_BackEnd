package space.space_spring.domain.discord.application.port.out;

public interface CreateDiscordRolePort {

    boolean createRole(Long discordGuildId,String name,Integer rgb);

    boolean createRole(Long discordGuildId,String name,Integer rgb,boolean botSelf);
    boolean createAndAddRole(Long discordGuildId,String name,Integer rgb,Long memberId);

    boolean createAndAddRole(Long discordGuildId,String name,Integer rgb,Long memberId,boolean botSelf);
}
