package space.space_spring.domain.discord.adapter.in.discord;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.post.application.port.out.LoadBoardCachePort;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageCreateEventListener extends ListenerAdapter {
    private final LoadBoardCachePort loadBoardCachePort;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        if(event.getAuthor().isBot()){
            return;
        }

        Optional<Long> boardId = loadBoardCachePort.findByDiscordId(event.getChannel().getIdLong());
        if(boardId.isEmpty()){
            return;
        }

        //ToDo 채널 분류 후 useCase 호출 
    }
}
