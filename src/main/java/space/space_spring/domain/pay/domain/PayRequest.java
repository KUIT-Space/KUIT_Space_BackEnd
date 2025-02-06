package space.space_spring.domain.pay.domain;

import lombok.Getter;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.global.util.NaturalNumber;

@Getter
public class PayRequest {

    private Long id;

    private SpaceMember payCreator;

    private Money totalAmount;

    private Money receivedAmount;

    private NaturalNumber totalTargetNum;

    private NaturalNumber sendCompleteTargetNum;

    private Bank bank;

    private boolean isComplete;

    private PayType payType;

    private PayRequest(Long id, SpaceMember payCreator, Money totalAmount, Money receivedAmount, NaturalNumber totalTargetNum, NaturalNumber sendCompleteTargetNum, Bank bank, boolean isComplete, PayType payType) {
        this.id = id;
        this.payCreator = payCreator;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.totalTargetNum = totalTargetNum;
        this.sendCompleteTargetNum = sendCompleteTargetNum;
        this.bank = bank;
        this.isComplete = isComplete;
        this.payType = payType;
    }

    public static PayRequest createNewPayRequest(Long id, SpaceMember payCreator, Money totalAmount, NaturalNumber totalTargetNum, Bank bank, PayType payType) {
        return new PayRequest(id, payCreator, totalAmount, Money.of(0), totalTargetNum, NaturalNumber.of(0), bank, false, payType);
    }

    public static PayRequest withoutId(SpaceMember payCreator, Money totalAmount, NaturalNumber totalTargetNum, Bank bank, PayType payType) {
        return new PayRequest(null, payCreator, totalAmount, Money.of(0), totalTargetNum, NaturalNumber.of(0), bank, false, payType);
    }

    public static PayRequest of(Long id, SpaceMember payCreator, Money totalAmount, Money receivedAmount, NaturalNumber totalTargetNum, NaturalNumber sendCompleteTargetNum, Bank bank, boolean isComplete, PayType payType) {
        return new PayRequest(id, payCreator, totalAmount, receivedAmount, totalTargetNum, sendCompleteTargetNum, bank, isComplete, payType);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void changeToComplete() {
        this.isComplete = true;
    }

    public void changeToIncomplete() {
        this.isComplete = false;
    }
}
