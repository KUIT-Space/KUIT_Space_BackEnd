package space.space_spring.domain.discord.adapter.in.discord;

import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.application.port.out.LoadBoardCachePort;

import java.util.Optional;
@Component
@RequiredArgsConstructor
public class MessageUpdateEventListener extends ListenerAdapter {
    private final DiscordUtil discordUtil;
    private final LoadBoardCachePort loadBoardCachePort;
    @Override
    public void onMessageUpdate(MessageUpdateEvent event){
        if(event.getAuthor().isBot()){
            //log.info("bot message. ignore");
            return;
        }

        MessageChannelUnion channel=event.getChannel();

        if(!isAvailableChannelType(channel))
        {
            //log.info("not Available channel type. ignore");
            return;
        }

        Long parentChannelId = discordUtil.getRootChannelId(channel);
        //log.info("parentChannelId:"+parentChannelId);

        Optional<Long> boardId = loadBoardCachePort.findByDiscordId(parentChannelId);

        if(boardId.isEmpty()){
            //log.info("not in cache. ignore");
            return;
        }

        if(isComment(event.getChannel())){
            //Todo map comment update command
            //Todo update comment UseCase call

        }

        //Todo map post update command
        //Todo update post UseCase call







    }

    private boolean isComment(MessageChannelUnion channel){
        return channel.getType().equals(ChannelType.GUILD_PUBLIC_THREAD);
    }

    private boolean isAvailableChannelType(MessageChannelUnion channel){
        switch (channel.getType()){
            case TEXT :
            case FORUM:
            case GUILD_PUBLIC_THREAD:
                return true;
            default:
                return false;
        }
    }
}
