package space.space_spring.domain.discord.adapter.out.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CheckDiscordChannelPort;
@Component
@RequiredArgsConstructor
public class DiscordChannelAdapter implements CheckDiscordChannelPort {

    private final JDA jda;

    @Override
    public boolean isTextChannel(Long channelId){
        TextChannel channel = jda.getTextChannelById(channelId);
        return channel != null;
    }

    @Override
    public boolean isForumChannel(Long channelId){
        ForumChannel channel = jda.getForumChannelById(channelId);
        return channel != null;
    }
    @Override
    public Long getRootChannelId(Long channelId){
//        TextChannel channel = jda.getTextChannelById(channelId);
//        if(isTextChannel(channelId)){
//            return channelId;
//        }
//        ForumChannel forumChannel = jda.getForumChannelById(channelId);
//        if(!(forumChannel==null)){
//            return channelId;
//        }
        ThreadChannel threadChannel = jda.getChannelById(ThreadChannel.class, channelId);
        if(!(threadChannel==null)){
            return threadChannel.getParentChannel().getIdLong();
        }
        return channelId;

    }

    @Override
    public String getChannelType(Long guildId, Long channelId){

        return jda.getGuildById(guildId).getChannelCache().getElementById(channelId).getType().toString();

    }

    @Override
    public boolean isThreadExist(Long guildId, Long channelId){
        if(jda.getThreadChannelById(channelId)==null){
            return false;
        }
        return true;
    }
}
