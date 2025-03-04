package space.space_spring.domain.pay.adapter.in.web.readPayHome;

import lombok.Getter;
import space.space_spring.domain.pay.application.port.in.readPayHome.InfoOfPayRequestInHome;

@Getter
public class PayRequestInfoInHome {

    private Long payRequestId;

    private int totalAmount;

    private int receivedAmount;

    private int totalTargetNum;

    private int sendCompleteTargetNum;

    private PayRequestInfoInHome(Long payRequestId, int totalAmount, int receivedAmount, int totalTargetNum, int sendCompleteTargetNum) {
        this.payRequestId = payRequestId;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.totalTargetNum = totalTargetNum;
        this.sendCompleteTargetNum = sendCompleteTargetNum;
    }

    public static PayRequestInfoInHome of(InfoOfPayRequestInHome infoOfPayRequestInHome) {
        return new PayRequestInfoInHome(
                infoOfPayRequestInHome.getPayRequestId(),
                infoOfPayRequestInHome.getTotalAmount().getAmountInInteger(),
                infoOfPayRequestInHome.getReceivedAmount().getAmountInInteger(),
                infoOfPayRequestInHome.getTotalTargetNum().getNumber(),
                infoOfPayRequestInHome.getSendCompleteTargetNum().getNumber()
        );
    }
}
