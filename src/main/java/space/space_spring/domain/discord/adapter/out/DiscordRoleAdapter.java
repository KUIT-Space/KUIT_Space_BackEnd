package space.space_spring.domain.discord.adapter.out;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CreateDiscordRolePort;
import space.space_spring.global.exception.CustomException;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.FAIL_ROLE_CREATE;

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
    public boolean createRole(Long guildId,String name,Integer rgb,boolean botSelf) {
        if(!botSelf){
            createRole(guildId,name,rgb);
        }
        boolean already = createRole(guildId,name,rgb);
        Role role = getRoleExist(guildId,name).stream().findFirst().orElseThrow(()-> new CustomException(FAIL_ROLE_CREATE));
        addRoleBotSelf(jda.getGuildById(guildId),role);
        return already;

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
        addRole(guild,memberId,role);

        return true;
    }


    @Override
    public boolean createAndAddRole(Long guildId,String name,Integer rgb,Long memberId,boolean botSelf) {
        if(!botSelf){
            createAndAddRole(guildId,name,rgb,memberId);
        }
        boolean already = createAndAddRole(guildId,name,rgb,memberId);
        Role role = getRoleExist(guildId,name).stream().findFirst().orElseThrow(()-> new CustomException(FAIL_ROLE_CREATE));
        System.out.println("find role: "+role.getName());
        addRoleBotSelf(jda.getGuildById(guildId),role);
        return already;
    }
    private Role createRoleInDiscord(Long guildId,String name,Integer rgb){
        return jda.getGuildById(guildId).createRole().setName(name).setColor(rgb).complete();
    }

    private List<Role> getRoleExist(Long guildId, String name){
        return jda.getGuildById(guildId).getRolesByName(name,false);

    }
    private void addRole(Guild guild,Long memberId,Role role){
        guild.addRoleToMember(jda.getUserById(memberId),role).complete();
    }

    private void addRoleBotSelf(Guild guild,Role role){
        Member bot = guild.getSelfMember();
        addRole(guild,bot.getIdLong(),role);
    }
}
