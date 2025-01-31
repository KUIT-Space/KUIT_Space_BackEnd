package space.space_spring.domain.pay.application.port.in.readRequestedPayList;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;

@Getter
public class InfoOfRequestedPay {

    private Long payRequestedTargetId;

    private String payCreatorName;

    // 정산생성자의 프로필 이미지까지 ??

    private Money requestedAmount;

    private Bank bank;          // 송금할 은행 정보

    private InfoOfRequestedPay(Long payRequestedTargetId, String payCreatorName, Money requestedAmount, Bank bank) {
        this.payRequestedTargetId = payRequestedTargetId;
        this.payCreatorName = payCreatorName;
        this.requestedAmount = requestedAmount;
        this.bank = bank;
    }

    public static InfoOfRequestedPay of(Long payRequestedTargetId, String payCreatorName, Money requestedAmount, Bank bank) {
        return new InfoOfRequestedPay(payRequestedTargetId, payCreatorName, requestedAmount, bank);
    }

}
