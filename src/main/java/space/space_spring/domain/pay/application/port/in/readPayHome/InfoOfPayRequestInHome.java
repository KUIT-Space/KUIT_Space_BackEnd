package space.space_spring.domain.pay.application.port.in.readPayHome;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.global.util.NaturalNumber;

@Getter
public class InfoOfPayRequestInHome {

    private Long payRequestId;

    private Money totalAmount;

    private Money receivedAmount;

    private NaturalNumber totalTargetNum;

    private NaturalNumber sendCompleteTargetNum;

    private InfoOfPayRequestInHome(Long payRequestId, Money totalAmount, Money receivedAmount, NaturalNumber totalTargetNum, NaturalNumber sendCompleteTargetNum) {
        this.payRequestId = payRequestId;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.totalTargetNum = totalTargetNum;
        this.sendCompleteTargetNum = sendCompleteTargetNum;
    }

    public static InfoOfPayRequestInHome of(Long payRequestId, Money totalAmount, Money receivedAmount, NaturalNumber totalTargetNum, NaturalNumber sendCompleteTargetNum) {
        return new InfoOfPayRequestInHome(payRequestId, totalAmount, receivedAmount, totalTargetNum, sendCompleteTargetNum);
    }
}
