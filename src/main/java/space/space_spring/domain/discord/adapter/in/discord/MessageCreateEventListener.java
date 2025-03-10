package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.in.discord.InputMessageFromDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.discord.MessageInputFromDiscordCommand;
import space.space_spring.domain.discord.application.service.MessageInputFromDiscordService;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardCommand;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardCachePort;
import space.space_spring.domain.post.domain.BoardType;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageCreateEventListener extends ListenerAdapter {
    private final LoadBoardCachePort loadBoardCachePort;
    private final DiscordUtil discordUtil;
    private final InputMessageFromDiscordUseCase inputMessageFromDiscordUseCase;
    private final DiscordMessageMapper discordMessageMapper;
    private final CreateBoardUseCase createBoardUseCase;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){

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
        MessageInputFromDiscordCommand command = discordMessageMapper.mapToCommand(event,boardId.get());
        //log.info(command.toString());
        inputMessageFromDiscordUseCase.putPost(command);
        inputMessageFromDiscordUseCase.putComment(command,boardId.get());
        //ToDo 채널 분류 후 useCase 호출

    }

    private boolean isAvailableChannelType(MessageChannelUnion channel){
        switch (channel.getType()){
            case TEXT :
            case FORUM:
            case GUILD_PUBLIC_THREAD:
            case PRIVATE:
                return true;
            default:
                return false;
        }
    }


}
