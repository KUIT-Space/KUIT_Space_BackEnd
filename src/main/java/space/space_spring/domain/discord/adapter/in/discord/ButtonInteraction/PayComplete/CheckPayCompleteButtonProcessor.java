package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.PayComplete;

import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
import space.space_spring.domain.pay.application.port.in.validatePayTarget.ValidatePayTargetUseCase;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.space.application.port.in.LoadSpaceUseCase;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

@Component
@RequiredArgsConstructor
public class CheckPayCompleteButtonProcessor implements ButtonInteractionProcessor {
    private final LoadPayRequestPort loadPayRequestPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final ValidatePayTargetUseCase validatePayTargetUseCase;
    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("check:pay-complete:")){
            return true;
        }
        return false;
    }
    @Override
    public void process(ButtonInteractionEvent event){
        String buttonId = event.getComponentId();
        String[] parts = buttonId.split(":");
        if (parts.length < 3) return;

        if(!validatePayTarget(event.getGuild().getIdLong(),Long.valueOf(parts[2]),event.getMember().getIdLong())){
            event.reply("이 정산의 대상자가 아닙니다")
                    .setEphemeral(true)
                    .queue();
            return;
        }

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

    private boolean validatePayTarget(Long guildId,Long payRequestDiscordId,Long discordMemberId){
        //Long guildId = event.getGuild().getIdLong();
        Long payRequestId = loadPayRequestPort.loadByDiscordId(payRequestDiscordId).getId();
        Long spaceMemberId = loadSpaceMemberPort.loadByDiscord(guildId,discordMemberId).getId();
        return validatePayTargetUseCase.hasPayTarget(spaceMemberId, payRequestId);
    }

}
