package space.space_spring.domain.pay.adapter.in.web.readPayHome;

import lombok.Getter;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.InfoOfPayRequest;

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

    public static PayRequestInfoInHome of(InfoOfPayRequest infoOfPayRequest) {
        return new PayRequestInfoInHome(
                infoOfPayRequest.getPayRequestId(),
                infoOfPayRequest.getTotalAmount().getAmountInInteger(),
                infoOfPayRequest.getReceivedAmount().getAmountInInteger(),
                infoOfPayRequest.getTotalTargetNum().getNumber(),
                infoOfPayRequest.getSendCompleteTargetNum().getNumber()
        );
    }
}
