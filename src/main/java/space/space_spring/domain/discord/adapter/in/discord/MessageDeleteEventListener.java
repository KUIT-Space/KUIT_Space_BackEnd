package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.in.deleteMessageFromDiscordUseCase.DeleteMessageUseCase;
import space.space_spring.domain.post.application.port.out.LoadPostBasePort;

import java.util.Optional;
@Component
@RequiredArgsConstructor
public class MessageDeleteEventListener extends ListenerAdapter {
    private final DeleteMessageUseCase deleteMessageUseCase;
    @Override
    public void onMessageDelete(MessageDeleteEvent event){
        Long messageId = event.getMessageIdLong();


        MessageChannelUnion channel=event.getChannel();

        if(!isAvailableChannelType(channel))
        {
            //log.info("not Available channel type. ignore");
            return;
        }
        if(!event.isFromThread()){
            deleteMessageUseCase.deletePost(messageId);
            return;
        }

        if((event.getChannel().getIdLong()==event.getMessageIdLong())){
            deleteMessageUseCase.deletePost(messageId);
            return;
        }
        deleteMessageUseCase.deleteComment(messageId);
        return;

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
