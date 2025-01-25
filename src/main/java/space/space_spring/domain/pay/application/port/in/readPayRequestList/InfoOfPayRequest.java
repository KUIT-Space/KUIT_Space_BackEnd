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

    private InfoOfPayRequest(Long payRequestId, Money totalAmount, Money receivedAmount, NaturalNumber totalTargetNum, NaturalNumber sendCompleteTargetNum) {
        this.payRequestId = payRequestId;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.totalTargetNum = totalTargetNum;
        this.sendCompleteTargetNum = sendCompleteTargetNum;
    }

    public static InfoOfPayRequest of(Long payRequestId, Money totalAmount, Money receivedAmount, NaturalNumber totalTargetNum, NaturalNumber sendCompleteTargetNum) {
        return new InfoOfPayRequest(payRequestId, totalAmount, receivedAmount, totalTargetNum, sendCompleteTargetNum);
    }

}
