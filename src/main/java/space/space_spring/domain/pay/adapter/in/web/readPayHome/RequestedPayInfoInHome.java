package space.space_spring.domain.pay.adapter.in.web.readPayHome;

import lombok.Getter;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.InfoOfRequestedPay;

@Getter
public class RequestedPayInfoInHome {

    private Long payRequestedTargetId;

    private String payCreatorName;

    private String payCreatorProfileImgUrl;

    private int requestedAmount;

    private RequestedPayInfoInHome(Long payRequestedTargetId, String payCreatorName, int requestedAmount) {
        this.payRequestedTargetId = payRequestedTargetId;
        this.payCreatorName = payCreatorName;
        this.requestedAmount = requestedAmount;
    }

    public static RequestedPayInfoInHome of(InfoOfRequestedPay infoOfRequestedPay) {
        return new RequestedPayInfoInHome(
                infoOfRequestedPay.getPayRequestTargetId(),
                infoOfRequestedPay.getPayCreatorNickname(),
                infoOfRequestedPay.getRequestedAmount().getAmountInInteger()
        );
    }
}
