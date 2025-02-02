package space.space_spring.domain.pay.adapter.in.web.readRequestedPayList;

import space.space_spring.domain.pay.application.port.in.readRequestedPayList.InfoOfRequestedPay;

public class ResponseOfRequestedPayInfo {

    private Long payRequestedTargetId;

    private String payCreatorName;

    // 정산생성자의 프로필 이미지까지 ??

    private int requestedAmount;

    private String bankName;                // 송금할 은행 정보

    private String bankAccountNum;          // 송금할 은행 정보

    private ResponseOfRequestedPayInfo(Long payRequestedTargetId, String payCreatorName, int requestedAmount, String bankName, String bankAccountNum) {
        this.payRequestedTargetId = payRequestedTargetId;
        this.payCreatorName = payCreatorName;
        this.requestedAmount = requestedAmount;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
    }

    public static ResponseOfRequestedPayInfo of(InfoOfRequestedPay infoOfRequestedPay) {
        return new ResponseOfRequestedPayInfo(
                infoOfRequestedPay.getPayRequestTargetId(),
                infoOfRequestedPay.getPayCreatorNickname(),
                infoOfRequestedPay.getRequestedAmount().getAmountInInteger(),
                infoOfRequestedPay.getBank().getName(),
                infoOfRequestedPay.getBank().getAccountNumber()
        );
    }
}
