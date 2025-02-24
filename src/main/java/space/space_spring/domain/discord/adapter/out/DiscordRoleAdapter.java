package space.space_spring.domain.discord.adapter.out;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CreateDiscordRolePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscordRoleAdapter implements CreateDiscordRolePort {
    private final JDA jda;
    @Override
    public boolean createRole(Long guildId,String name,Integer rgb){
        if(getRoleExist(guildId,name).isEmpty()){
            createRoleInDiscord(guildId, name, rgb);
            return true;
        }else{
            return false;
        }
    }
    @Override
    public boolean createAndAddRole(Long guildId,String name,Integer rgb,Long memberId){
        Guild guild=jda.getGuildById(guildId);

        List<Role> roles = getRoleExist(guildId, name);
        Role role;
        if(roles.isEmpty()){
            role=createRoleInDiscord(guildId, name, rgb);
        }else{
            role=roles.get(0);
        }
        guild.addRoleToMember(jda.getUserById(memberId),role).complete();

        return true;
    }
    private Role createRoleInDiscord(Long guildId,String name,Integer rgb){
        return jda.getGuildById(guildId).createRole().setName(name).setColor(rgb).complete();
    }

    private List<Role> getRoleExist(Long guildId, String name){
        return jda.getGuildById(guildId).getRolesByName(name,true);

    }
}
