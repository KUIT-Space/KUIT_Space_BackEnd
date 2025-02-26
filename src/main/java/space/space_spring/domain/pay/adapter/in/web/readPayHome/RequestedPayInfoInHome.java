package space.space_spring.domain.pay.adapter.in.web.readPayHome;

import lombok.Getter;
import space.space_spring.domain.pay.application.port.in.readPayHome.InfoOfRequestedPayInHome;

@Getter
public class RequestedPayInfoInHome {

    private Long payRequestedTargetId;

    private String payCreatorName;

    private String payCreatorProfileImgUrl;

    private int requestedAmount;

    private RequestedPayInfoInHome(Long payRequestedTargetId, String payCreatorName, String payCreatorProfileImgUrl, int requestedAmount) {
        this.payRequestedTargetId = payRequestedTargetId;
        this.payCreatorName = payCreatorName;
        this.payCreatorProfileImgUrl = payCreatorProfileImgUrl;
        this.requestedAmount = requestedAmount;
    }

    public static RequestedPayInfoInHome of(InfoOfRequestedPayInHome infoOfRequestedPayInHome) {
        return new RequestedPayInfoInHome(
                infoOfRequestedPayInHome.getPayRequestTargetId(),
                infoOfRequestedPayInHome.getPayCreatorNickname(),
                infoOfRequestedPayInHome.getPayCreatorProfileImageUrl(),
                infoOfRequestedPayInHome.getRequestedAmount().getAmountInInteger()
        );
    }
}
