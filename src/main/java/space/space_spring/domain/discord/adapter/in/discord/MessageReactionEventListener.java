package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.application.port.out.LoadBoardCachePort;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageReactionEventListener extends ListenerAdapter {
    //private final Like
    private final DiscordUtil discordUtil;
    private final LoadBoardCachePort loadBoardCachePort;
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){


        Optional<Long> boardId = getBoardId(event);
        if(boardId.isEmpty()){
            return;
        }

        Long guildId=event.getGuild().getIdLong();
        Long messageId = event.getMessageIdLong();
        Long memberId = event.getMember().getIdLong();

        //Todo 좋아요 누르는 UseCase 호출

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event){


        Optional<Long> boardId = getBoardId(event);
        if(boardId.isEmpty()){
            return;
        }

        Long guildId=event.getGuild().getIdLong();
        Long messageId = event.getMessageIdLong();
        Long memberId = event.getMember().getIdLong();

        //Todo 좋아요  삭제하는 UseCase 호출
        
    }

    private Optional<Long> getBoardId(GenericMessageEvent event){
        MessageChannelUnion channel=event.getChannel();
        Long parentChannelId = discordUtil.getRootChannelId(channel);
        //log.info("parentChannelId:"+parentChannelId);

        Optional<Long> boardId = loadBoardCachePort.findByDiscordId(parentChannelId);

    }
}
