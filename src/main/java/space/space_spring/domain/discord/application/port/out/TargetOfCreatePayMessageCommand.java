package space.space_spring.domain.discord.application.port.out;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Money;

@Getter
public class TargetOfCreatePayMessageCommand {

    private Long targetMemberDiscordId;

    private Money requestedAmount;

    private TargetOfCreatePayMessageCommand(Long targetMemberDiscordId, Money requestedAmount) {
        this.targetMemberDiscordId = targetMemberDiscordId;
        this.requestedAmount = requestedAmount;
    }

    public static TargetOfCreatePayMessageCommand create(Long targetMemberDiscordId, Money requestedAmount) {
        return new TargetOfCreatePayMessageCommand(targetMemberDiscordId, requestedAmount);
    }
}
