package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.PayComplete;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
import space.space_spring.domain.pay.application.port.in.completePay.CompletePayUseCase;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.ALREADY_COMPLETE_PAY_REQUEST_TARGET;

@Component
@RequiredArgsConstructor
@Slf4j
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
        String buttonId = event.getComponentId();
        String[] parts = buttonId.split(":");
        Long payRequestDiscordId = Long.valueOf(parts[1]);
        Long guildId = event.getGuild().getIdLong();
        SpaceMember spaceMember = loadSpaceMemberPort.loadByDiscord(guildId,event.getMember().getIdLong());
        Long spaceMemberId = spaceMember.getId();

        try {
            completePayUseCase.completeForRequestedPay(
                    spaceMemberId,
                    loadPayRequestTargetPort.loadByTargetMemberId(spaceMemberId).stream()
                            .filter(payTarget-> {
                                return loadPayRequestPort.loadById(payTarget.getPayRequestId()).getDiscordMessageId().equals(payRequestDiscordId);
                            }).findFirst().orElseThrow().getId());

        } catch (CustomException e) {
            if (e.getMessage().equals(ALREADY_COMPLETE_PAY_REQUEST_TARGET.getMessage())) {
                event.reply("이미 정산 완료 처리 하셨습니다").setEphemeral(true).queue();

                return;
            }
            log.error("pay complete in discord error:"+e.getMessage());
            throw e;
        }
        event.reply("정산 완료 처리 되었습니다")
                .setEphemeral(true)
                .queue();
    }
}
