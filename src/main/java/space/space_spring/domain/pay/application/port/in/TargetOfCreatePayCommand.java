package space.space_spring.domain.pay.application.port.in;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Money;

@Getter
public class TargetOfCreatePayCommand {

    private Long targetMemberId;

    private Money requestedAmount;

    private TargetOfCreatePayCommand(Long targetMemberId, Money requestedAmount) {
        this.targetMemberId = targetMemberId;
        this.requestedAmount = requestedAmount;
    }

    public static TargetOfCreatePayCommand create(Long targetMemberId, Money requestedAmount) {
        return new TargetOfCreatePayCommand(targetMemberId, requestedAmount);
    }

}
