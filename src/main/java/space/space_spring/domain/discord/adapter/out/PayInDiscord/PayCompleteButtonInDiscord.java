package space.space_spring.domain.discord.adapter.out.PayInDiscord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.Pay.CreatePayCompleteButtonPort;

@Component
@RequiredArgsConstructor
public class PayCompleteButtonInDiscord implements CreatePayCompleteButtonPort {
    private final JDA jda;


    public void sendCompleteButton(Long threadId){

        ThreadChannel threadChannel =jda.getThreadChannelById(threadId);

        threadChannel.sendMessage("돈을 송금 하셨으면 버튼을 눌러 정산 완료 처리 해주세요.\n혹은 space web에서 완료 처리 해주세요")
                .addComponents(
                        ActionRow.of(Button.primary("check:pay-complete:"+threadId,"정산 완료"))
                )
                .queue();
    }
}
