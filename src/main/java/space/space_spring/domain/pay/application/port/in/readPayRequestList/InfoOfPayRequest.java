package space.space_spring.domain.pay.application.port.in.readPayRequestList;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.global.util.NaturalNumber;

@Getter
public class InfoOfPayRequest {

    private Long payRequestId;

    private Money totalAmount;

    private Money receivedAmount;

    private NaturalNumber totalTargetNum;

    private NaturalNumber sendCompleteTargetNum;

}
