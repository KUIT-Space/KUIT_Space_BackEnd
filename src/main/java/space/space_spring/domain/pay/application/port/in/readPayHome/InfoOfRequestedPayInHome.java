package space.space_spring.domain.pay.application.port.in.readPayHome;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;

@Getter
public class InfoOfRequestedPayInHome {

    private Long payRequestTargetId;

    private String payCreatorNickname;

    private String payCreatorProfileImageUrl;

    private Money requestedAmount;

    private Bank bank;          // 송금할 은행 정보

    private InfoOfRequestedPayInHome(Long payRequestTargetId, String payCreatorNickname, String payCreatorProfileImageUrl, Money requestedAmount, Bank bank) {
        this.payRequestTargetId = payRequestTargetId;
        this.payCreatorNickname = payCreatorNickname;
        this.payCreatorProfileImageUrl = payCreatorProfileImageUrl;
        this.requestedAmount = requestedAmount;
        this.bank = bank;
    }

    public static InfoOfRequestedPayInHome of(Long payRequestTargetId, String payCreatorNickname, String payCreatorProfileImageUrl, Money requestedAmount, Bank bank) {
        return new InfoOfRequestedPayInHome(payRequestTargetId, payCreatorNickname, payCreatorProfileImageUrl, requestedAmount, bank);
    }
}
