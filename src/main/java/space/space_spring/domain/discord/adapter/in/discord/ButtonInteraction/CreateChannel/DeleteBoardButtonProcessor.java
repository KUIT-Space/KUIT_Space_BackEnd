package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.CreateChannel;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
@Component
@RequiredArgsConstructor
public class DeleteBoardButtonProcessor implements ButtonInteractionProcessor {

    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("delete-board")){
             return true;
        }
        return false;
    }

    @Override
    public void process(ButtonInteractionEvent event){
        // Todo 현재 등록된 게시판 전부 가져오기

    }
}
