package space.space_spring.domain.discord.adapter.out;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CreateDiscordRolePort;

@Component
@RequiredArgsConstructor
public class DiscordRoleAdapter implements CreateDiscordRolePort {
    private final JDA jda;
    @Override
    public boolean createRole(Long guildId,String name,Integer rgb){
        jda.getGuildById(guildId).createRole().setName(name).setColor(rgb).complete();
        return true;
    }
    @Override
    public boolean createAndAddRole(Long guildId,String name,Integer rgb,Long memberId){
        Guild guild=jda.getGuildById(guildId);
        Role role = guild.createRole().setName(name).setColor(rgb).complete();
        guild.addRoleToMember(jda.getUserById(memberId),role).complete();

        return true;
    }
}
