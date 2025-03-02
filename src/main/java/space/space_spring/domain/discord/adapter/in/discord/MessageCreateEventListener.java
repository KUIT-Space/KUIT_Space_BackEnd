package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.application.port.out.LoadBoardCachePort;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageCreateEventListener extends ListenerAdapter {
    private final LoadBoardCachePort loadBoardCachePort;
    private final DiscordUtil discordUtil;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        if(event.getAuthor().isBot()){
            return;
        }

        MessageChannelUnion channel=event.getChannel();

        if(isAvailableChannelType(channel))
        {
            return;
        }

        Long parentChannelId = discordUtil.getRootChannelId(channel);

        ChannelType parentChannelType;
//        if(event.getChannelType().equals(ChannelType.GUILD_PUBLIC_THREAD)){
//            Channel parentChannel = channel.asThreadChannel().getParentChannel();
//            parentChannelType=parentChannel.getType();
//
//        }
        Optional<Long> boardId = loadBoardCachePort.findByDiscordId(parentChannelId);

        if(boardId.isEmpty()){
            return;
        }

        //ToDo 채널 분류 후 useCase 호출
    }

    private boolean isAvailableChannelType(MessageChannelUnion channel){
        switch (channel.getType()){
            case TEXT :
            case FORUM:
            case GUILD_PUBLIC_THREAD:
            case PRIVATE:
            case CATEGORY:
                return true;
            default:
                return false;
        }
    }
    private boolean isFromTextThread(){

    }

}
