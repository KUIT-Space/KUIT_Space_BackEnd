package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageReactionEventListener extends ListenerAdapter {
    //private final Like
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){

        //Todo 게시판 채널에서 들어온 리액션인지 확인

        Long guildId=event.getGuild().getIdLong();
        Long messageId = event.getMessageIdLong();
        Long memberId = event.getMember().getIdLong();

        //Todo 좋아요 누르는 UseCase 호출

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event){
        //Todo 게시판 채널에서 들어온 리액션인지 확인

        Long guildId=event.getGuild().getIdLong();
        Long messageId = event.getMessageIdLong();
        Long memberId = event.getMember().getIdLong();

        //Todo 좋아요  삭제하는 UseCase 호출
    }
}
