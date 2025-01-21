package space.space_spring.domain.pay.application.port.in;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Money;

@Getter
public class TargetOfCreatePayDto {

    // 네이밍 다시

    private Long targetMemberId;

    private Money requestedAmount;


}
