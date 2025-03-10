package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.CreateChannel;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
@Component
@RequiredArgsConstructor
public class CheckMoveCurrentChannelMessageButtonProcessor implements ButtonInteractionProcessor {
    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("check:move-current-message")){
            return true;
        }
        return false;
    }

    @Override
    public void process(ButtonInteractionEvent event){
        String buttonId = event.getComponentId();

        int index = buttonId.indexOf(":"); // 첫 번째 ":"의 위치 찾기
        String returnString = (index != -1) ? buttonId.substring(index + 1) : buttonId; // ":" 이후 부분 반환


        event.reply("이 게시판의 이전 글들을 SPACE에 **저장**하시겠습니까?")
                .addActionRow(
                        Button.primary(returnString, " 예 "),
                        Button.secondary("cancel", "아니오")
                ).setEphemeral(true)
                .queue();
    }



}
