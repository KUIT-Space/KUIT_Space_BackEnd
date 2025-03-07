package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.createPost.CreatePostMessagePort;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;

@Component
@RequiredArgsConstructor
public class MoveMessageEventListener extends ListenerAdapter {
    private final LoadBoardPort loadBoardPort;
    private final CreatePostUseCase createPostUseCase;
    private final CreatePostMessagePort CreatePostMessagePort;
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        if(!event.getName().equals("move-message")){
            return;
        }

        //Todo redis cache 로 바꾸기




    }


    //성공 여부 반환
    private boolean moveChannel(){
        return true;
    }

    //몇개를 저장 성공했는지 반환
    private int savePost(){

        return 0;
    }
}
