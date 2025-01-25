package space.space_spring.domain.pay.domain;

import lombok.Getter;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.global.util.NaturalNumber;

@Getter
public class PayRequest {

    private SpaceMember payCreator;

    private Money totalAmount;

    private Money receivedAmount;

    private NaturalNumber totalTargetNum;

    private NaturalNumber sendCompleteTargetNum;

    private Bank bank;

    private boolean isComplete;

    private PayType payType;

    private PayRequest(SpaceMember payCreator, Money totalAmount, NaturalNumber totalTargetNum, Bank bank, PayType payType) {
        this.payCreator = payCreator;
        this.totalAmount = totalAmount;
        this.receivedAmount = Money.of(0);
        this.totalTargetNum = totalTargetNum;
        this.sendCompleteTargetNum = NaturalNumber.of(0);
        this.bank = bank;
        this.isComplete = false;
        this.payType = payType;
    }

    public static PayRequest create(SpaceMember payCreator, Money totalAmount, NaturalNumber totalTargetNum, Bank bank, PayType payType) {
        return new PayRequest(payCreator, totalAmount, totalTargetNum, bank, payType);
    }
}
