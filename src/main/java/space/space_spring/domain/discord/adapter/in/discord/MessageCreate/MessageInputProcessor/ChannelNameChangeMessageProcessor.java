package space.space_spring.domain.discord.adapter.in.discord.MessageCreate.MessageInputProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.DiscordMessageMapper;
import space.space_spring.domain.discord.application.port.in.discord.InputMessageFromDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.discord.MessageInputFromDiscordCommand;
import space.space_spring.domain.post.application.port.in.updatePost.UpdatePostUseCase;
import space.space_spring.domain.post.application.port.out.UpdatePostPort;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChannelNameChangeMessageProcessor implements MessageInputProcessor{
    private final DiscordMessageMapper discordMessageMapper;
    private final InputMessageFromDiscordUseCase inputMessageFromDiscordUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    public boolean supports(MessageReceivedEvent event){
        if(event.isFromThread()&&event.getMessage().getType().equals(MessageType.CHANNEL_NAME_CHANGE)){
            return true;
        }
        return false;
    }
    public void process(MessageReceivedEvent event,Long boardId){

        updatePostUseCase.updateTitle(event.getChannel().getIdLong(),event.getMessage().getContentRaw());

    }
}
