package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class CancelButtonProcessor implements ButtonInteractionProcessor{

    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("cancel")){
            return true;
        }
        return false;
    }
    @Override
    public void process(ButtonInteractionEvent event){
        event.reply("취소 되었습니다").setEphemeral(true).queue();
    }
}
