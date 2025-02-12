package space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.global.util.NaturalNumber;

@Getter
public class CurrentPayRequestState {

    private final Money receivedAmount;                     // 현재까지 받은 금액

    private final NaturalNumber sendCompleteTargetNum;      // 현재까지 송금한 사람 수

    private CurrentPayRequestState(Money receivedAmount, NaturalNumber sendCompleteTargetNum) {
        this.receivedAmount = receivedAmount;
        this.sendCompleteTargetNum = sendCompleteTargetNum;
    }

    public static CurrentPayRequestState of(Money receivedAmount, NaturalNumber sendCompleteTargetNum) {
        return new CurrentPayRequestState(receivedAmount, sendCompleteTargetNum);
    }
}
