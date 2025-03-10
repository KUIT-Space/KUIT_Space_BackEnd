package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.CreateChannel;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
@Component
@RequiredArgsConstructor
public class CheckMoveMessageButtonProcessor implements ButtonInteractionProcessor {

    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("check:move-message:")){
            return true;
        }
        return false;
    }

    @Override
    public void process(ButtonInteractionEvent event){
        String buttonId = event.getComponentId();
        String[] parts = buttonId.split(":");
        if (parts.length < 4) return;
        String thisChannelName = event.getChannel().getName();
        String targetChannelName = parts[4];
        String menuType = parts[2];
        int index = buttonId.indexOf(":"); // 첫 번째 ":"의 위치 찾기
        String returnString = (index != -1) ? buttonId.substring(index + 1) : buttonId; // ":" 이후 부분 반환


        event.reply("**"+thisChannelName+"**에서 --> **"+
                        targetChannelName+"**으로 메세지를 복사하시겠습니까?"+
                        "\n(이 기능은 최대 100개의 message 까지 가능합니다")
                .addActionRow(
                        Button.primary(returnString, " 예 "),
                        Button.primary("cancel", "아니오")
                ).setEphemeral(true)
                .queue();
    }

}
