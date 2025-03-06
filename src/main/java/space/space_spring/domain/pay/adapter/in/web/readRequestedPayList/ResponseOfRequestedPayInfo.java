package space.space_spring.domain.pay.adapter.in.web.readRequestedPayList;

import lombok.Getter;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.InfoOfRequestedPay;

@Getter
public class ResponseOfRequestedPayInfo {

    private Long payRequestTargetId;

    private String payCreatorName;

    private String payCreatorProfileImageUrl;

    private int requestedAmount;

    private String bankName;                // 송금할 은행 정보

    private String bankAccountNum;          // 송금할 은행 정보

    private ResponseOfRequestedPayInfo(Long payRequestTargetId, String payCreatorName, String payCreatorProfileImageUrl, int requestedAmount, String bankName, String bankAccountNum) {
        this.payRequestTargetId = payRequestTargetId;
        this.payCreatorName = payCreatorName;
        this.payCreatorProfileImageUrl = payCreatorProfileImageUrl;
        this.requestedAmount = requestedAmount;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
    }

    public static ResponseOfRequestedPayInfo of(InfoOfRequestedPay infoOfRequestedPay) {
        return new ResponseOfRequestedPayInfo(
                infoOfRequestedPay.getPayRequestTargetId(),
                infoOfRequestedPay.getPayCreatorNickname(),
                infoOfRequestedPay.getPayCreatorProfileImageUrl(),
                infoOfRequestedPay.getRequestedAmount().getAmountInInteger(),
                infoOfRequestedPay.getBank().getName(),
                infoOfRequestedPay.getBank().getAccountNumber()
        );
    }
}
