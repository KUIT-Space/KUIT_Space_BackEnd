package space.space_spring.domain.pay.domain;

import lombok.Getter;
import space.space_spring.domain.spaceMember.SpaceMember;

@Getter
public class PayRequest {

    private SpaceMember payCreator;

    private Money totalAmount;

    private Bank bank;

    private Money receivedAmount;

    private boolean isComplete;

    private PayType payType;

    private PayRequest(SpaceMember payCreator, Money totalAmount, Bank bank, PayType payType) {
        this.payCreator = payCreator;
        this.totalAmount = totalAmount;
        this.bank = bank;
        this.receivedAmount = Money.of(0);
        this.isComplete = false;
        this.payType = payType;
    }

    public static PayRequest create(SpaceMember payCreator, Money totalAmount, Bank bank, PayType payType) {
        return new PayRequest(payCreator, totalAmount, bank, payType);
    }
}
