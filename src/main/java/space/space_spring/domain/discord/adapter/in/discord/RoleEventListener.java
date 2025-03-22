package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.domain.DiscordRole;
import space.space_spring.domain.spaceMember.application.port.in.UpdateSpaceMemberUseCase;

import java.util.List;
@Component
@RequiredArgsConstructor
public class RoleEventListener extends ListenerAdapter {
    private final UpdateSpaceMemberUseCase updateSpaceMemberUseCase;
    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event){
        List<Role> roles = event.getRoles();
        if(roles==null||roles.isEmpty()){
            return;
        }
        roles.forEach(role-> {
            if (role.getName().equals(DiscordRole.SPACE_MANAGER.toString())) {
                //System.out.println("\nspace manager add");
                updateSpaceMemberUseCase.updateManager(event.getGuild().getIdLong(),event.getMember().getIdLong(),true);
            }
        });
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event){
        List<Role> roles = event.getRoles();
        if(roles==null||roles.isEmpty()){
            return;
        }
        roles.forEach(role-> {
            if (role.getName().equals(DiscordRole.SPACE_MANAGER.toString())) {
                //System.out.println("\nspace manager remove");
                updateSpaceMemberUseCase.updateManager(event.getGuild().getIdLong(),event.getMember().getIdLong(),false);
            }
        });
    }
}
