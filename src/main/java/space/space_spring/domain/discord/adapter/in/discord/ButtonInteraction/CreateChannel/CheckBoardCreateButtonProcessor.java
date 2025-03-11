package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.CreateChannel;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
@Component
@RequiredArgsConstructor
public class CheckBoardCreateButtonProcessor implements ButtonInteractionProcessor {

    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("check:create-channel:")){
            return true;
        }
        return false;
    }

    @Override
    public void process(ButtonInteractionEvent event){
        String buttonId = event.getComponentId();
        String[] parts = buttonId.split(":");
        if (parts.length < 4) return;

        String channelName = parts[4];
        String menuType = parts[2];
        int index = buttonId.indexOf(":"); // 첫 번째 ":"의 위치 찾기
        String returnString = (index != -1) ? buttonId.substring(index + 1) : buttonId; // ":" 이후 부분 반환


        event.reply("**"+channelName+"**"+
                        "이 채널을 **"+menuType+"**으로 등록하시겠습니까?")
                .addActionRow(
                        Button.primary(returnString, " 예 "),
                        Button.primary("cancel", "아니오")
                ).queue();
    }
}
