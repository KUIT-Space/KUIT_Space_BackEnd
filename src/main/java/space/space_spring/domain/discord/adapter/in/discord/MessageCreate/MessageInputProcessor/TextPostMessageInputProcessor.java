package space.space_spring.domain.discord.adapter.in.discord.MessageCreate.MessageInputProcessor;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.DiscordMessageMapper;
import space.space_spring.domain.discord.application.port.in.discord.InputMessageFromDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.discord.MessageInputFromDiscordCommand;

@Component
@RequiredArgsConstructor
public class TextPostMessageInputProcessor implements MessageInputProcessor{
    private final DiscordMessageMapper discordMessageMapper;
    private final InputMessageFromDiscordUseCase inputMessageFromDiscordUseCase;
    public boolean supports(MessageReceivedEvent event){
        if(!event.isFromThread()&&event.isFromType(ChannelType.TEXT)){
            return true;
        }
        return false;
    }
    public void process(MessageReceivedEvent event,Long boardId){

        if(event.getMessage().getContentRaw().length()<20){
            //text channel의 메세지 글자수가 20자 미만 이라면 메세지 무시
            return;
        }

        MessageInputFromDiscordCommand command = discordMessageMapper.mapToPostCommandFromText(event.getMessage(),boardId);

        //input from TEXT Channel
        inputMessageFromDiscordUseCase.putPost(command);

    }
}
