package space.space_spring.domain.discord.adapter.in.discord.MessageCreate.MessageInputProcessor;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.DiscordMessageMapper;
import space.space_spring.domain.discord.adapter.in.discord.DiscordUtil;
import space.space_spring.domain.discord.application.port.in.discord.CommentInputFromDiscordCommand;
import space.space_spring.domain.discord.application.port.in.discord.InputMessageFromDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.discord.MessageInputFromDiscordCommand;

@Component
@RequiredArgsConstructor
public class CommentMessageInputProcessor implements MessageInputProcessor{
    private final DiscordMessageMapper discordMessageMapper;
    private final InputMessageFromDiscordUseCase inputMessageFromDiscordUseCase;

    private final DiscordUtil discordUtil;

    public boolean supports(MessageReceivedEvent event){
        if(event.isFromThread()&&!discordUtil.isThreadStartMessage(event.getMessage())){
            return true;
        }
        return false;
    }
    public void process(MessageReceivedEvent event,Long boardId){
       CommentInputFromDiscordCommand command = discordMessageMapper.mapToCommentCommand(event.getMessage(),boardId);


        inputMessageFromDiscordUseCase.putComment(command,event.getChannel().getIdLong());

    }
}
