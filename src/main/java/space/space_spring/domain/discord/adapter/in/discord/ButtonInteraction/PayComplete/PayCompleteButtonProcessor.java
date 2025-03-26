package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.PayComplete;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
import space.space_spring.domain.pay.application.port.in.completePay.CompletePayUseCase;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
@Component
@RequiredArgsConstructor
public class PayCompleteButtonProcessor implements ButtonInteractionProcessor {
    private final CompletePayUseCase completePayUseCase;
    private final LoadPayRequestTargetPort loadPayRequestTargetPort;
    private final LoadPayRequestPort loadPayRequestPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    public boolean supports(String buttonId){
        if (buttonId.startsWith("pay-complete:")){
            return true;
        }
        return false;
    }
    public void process(ButtonInteractionEvent event){
        Long guildId = event.getGuild().getIdLong();
        SpaceMember spaceMember = loadSpaceMemberPort.loadByDiscord(guildId,event.getMember().getIdLong());
        Long spaceMemberId = spaceMember.getId();
        completePayUseCase.completeForRequestedPay(
                spaceMemberId,
                loadPayRequestTargetPort.loadByTargetMemberId(spaceMemberId).stream()
                        .filter(payTarget-> {
                            return loadPayRequestPort.loadById(payTarget.getPayRequestId()).getDiscordMessageId().equals(event.getMessageIdLong());
                        }).findFirst().orElseThrow().getId());
        event.reply("정산 완료 처리 되었습니다")
                .setEphemeral(true)
                .queue();
    }
}
