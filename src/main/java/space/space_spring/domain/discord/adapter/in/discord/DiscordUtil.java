package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscordUtil {
    private final JDA jda;

    public Long getRootChannelId(MessageChannelUnion channel){
        switch (channel.getType()) {
            case GUILD_PUBLIC_THREAD :
            case GUILD_PRIVATE_THREAD:
                return channel.asThreadChannel().getParentChannel().getIdLong();
            case TEXT:
            default:
                return channel.getIdLong();

        }
    }

    public boolean isForumChannel(Long channelId){
        if(jda.getForumChannelById(channelId)==null){
            return false;
        }
        return true;
    }

    public boolean isThreadStartMessage(Message message){
        return message.getChannel().getId().equals(message.getId());
    }

}
