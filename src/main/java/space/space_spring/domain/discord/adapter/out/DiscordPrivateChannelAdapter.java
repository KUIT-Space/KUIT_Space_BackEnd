package space.space_spring.domain.discord.adapter.out;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CreateDiscordRolePort;
import space.space_spring.domain.discord.application.port.out.CreatePrivateDiscordChannelPort;
import space.space_spring.domain.discord.domain.DiscordRole;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DiscordPrivateChannelAdapter implements CreatePrivateDiscordChannelPort {

    private final JDA jda;
    private final CreateDiscordRolePort createDiscordRolePort;
    private final String managerChannelName = "space_manager_channel";
    private final String managerCategoryName = "SPACE";

    @Override
    public Long createPrivateChannel(Long guildId,Long userId){
        Guild guild =jda.getGuildById(guildId);
        Category managerCategory = getOrCreateCategory(guild,managerCategoryName);
        Role managerRole = getManagerRole(guild);

        return  getOrCreateChannel(guild,managerCategory,managerRole).getIdLong();
    }

    private Role getManagerRole(Guild guild){
        List<Role> roles = guild.getRolesByName(DiscordRole.SPACE_MANAGER.toString(), false);
        if(roles.isEmpty()){
            System.out.println("\nprivate channel flow : no space Manager Role in guild");
            createDiscordRolePort.createRole(guild.getIdLong(),DiscordRole.SPACE_MANAGER.toString(),DiscordRole.SPACE_MANAGER.getColor());
            return getManagerRole(guild);//recursive
        }

        return roles.get(0);
    }

    private Category getOrCreateCategory(Guild guild,String categoryName){
        List<Category> categories = guild.getCategoriesByName(categoryName,false);
        if(categories.isEmpty()){
            return guild.createCategory(managerCategoryName).complete();
        }
        return categories.get(0);
    }
    private Channel getOrCreateChannel(Guild guild,Category category,Role role){

//        List<GuildChannel> guildChannels = guild.getChannels(true).stream().filter(guildChannel -> {
//            return guildChannel.getName().equals(managerChannelName);
//        }).toList();
//        if(guildChannels.isEmpty()){
//            System.out.println("no match channel Name: in guild");
//            return createPrivateChannel(guild, category, role);
//        }

        List<TextChannel> channels = guild.getTextChannelsByName(managerChannelName.toLowerCase(),true);
        if(channels.isEmpty()){
            System.out.println("no match channel Name");
            return createPrivateChannel(guild, category, role);
        }

        Optional<TextChannel> targetChannel= channels.stream().filter(channel->{
            return channel.getParentCategory().getName().equals(managerCategoryName);
        }).findFirst();

        if(targetChannel.isEmpty()){
            System.out.println("match channel name but not match category");
            return createPrivateChannel(guild, category, role);
        }

        return (Channel) targetChannel.get();


    }

    private Channel createPrivateChannel(Guild guild,Category category,Role role){
        return guild.createTextChannel(managerChannelName,category)// @everyone 역할에 대해 채널 보기 및 메시지 작성 권한 거부
                .addPermissionOverride(guild.getPublicRole(), null,
                        EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND))
                // Manager 역할에 대해 채널 보기 및 메시지 작성 권한 허용
                .addPermissionOverride(role,
                        EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
                .complete();
    }


}
