package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.PayComplete;

import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
@Component
@RequiredArgsConstructor
public class CheckPayCompleteButtonProcessor implements ButtonInteractionProcessor {

    public boolean supports(String buttonId){
        if (buttonId.startsWith("check:pay-complete:")){
            return true;
        }
        return false;
    }
    public void process(ButtonInteractionEvent event){
        String buttonId = event.getComponentId();
        String[] parts = buttonId.split(":");
        if (parts.length < 3) return;


        int index = buttonId.indexOf(":"); // 첫 번째 ":"의 위치 찾기
        String returnString = (index != -1) ? buttonId.substring(index + 1) : buttonId; // ":" 이후 부분 반환


        event.reply("\n**정산 완료**처리 하시겠습니까??")
                .addActionRow(
                        Button.primary(returnString, " 예 "),
                        Button.primary("cancel", "아니오")
                )
                .setEphemeral(true)
                .queue();
    }

}
