package space.space_spring.domain.pay.adapter.in.web.readPayRequestList;

import lombok.Getter;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.InfoOfPayRequest;

@Getter
public class ResponseOfPayRequestInfo {

    private Long payRequestId;

    private int totalAmount;

    private int receivedAmount;

    private int totalTargetNum;

    private int sendCompleteTargetNum;

    private ResponseOfPayRequestInfo(Long payRequestId, int totalAmount, int receivedAmount, int totalTargetNum, int sendCompleteTargetNum) {
        this.payRequestId = payRequestId;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.totalTargetNum = totalTargetNum;
        this.sendCompleteTargetNum = sendCompleteTargetNum;
    }

    public static ResponseOfPayRequestInfo of(InfoOfPayRequest infoOfPayRequest) {
        return new ResponseOfPayRequestInfo(
                infoOfPayRequest.getPayRequestId(),
                infoOfPayRequest.getTotalAmount().getAmountInInteger(),
                infoOfPayRequest.getReceivedAmount().getAmountInInteger(),
                infoOfPayRequest.getTotalTargetNum().intValue(),
                infoOfPayRequest.getSendCompleteTargetNum().intValue()
        );
    }
}
