package space.space_spring.domain.discord.application.port.in.createPay;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Money;

@Getter
public class TargetOfCreatePayInDiscordCommand {

    private Long targetMemberId;

    private Money requestedAmount;

    private TargetOfCreatePayInDiscordCommand(Long targetMemberId, Money requestedAmount) {
        this.targetMemberId = targetMemberId;
        this.requestedAmount = requestedAmount;
    }

    public static TargetOfCreatePayInDiscordCommand create(Long targetMemberId, Money requestedAmount) {
        return new TargetOfCreatePayInDiscordCommand(targetMemberId, requestedAmount);
    }
}
