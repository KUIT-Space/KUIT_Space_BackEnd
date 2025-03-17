package space.space_spring.domain.discord.adapter.in.discord;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleEventListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event){

    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event){

    }
}
