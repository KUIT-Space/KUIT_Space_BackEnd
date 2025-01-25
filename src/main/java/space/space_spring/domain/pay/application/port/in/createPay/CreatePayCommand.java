package space.space_spring.domain.pay.application.port.in.createPay;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.domain.pay.adapter.in.web.createPay.TargetOfPayRequest;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreatePayCommand {

    /**
     * 유스케이스 입력 모델 -> 유스케이스로의 유효성 검사 (필요하다면)
     */

    private Long payCreatorId;

    private Money totalAmount;

    private Bank bank;

    private List<TargetOfCreatePayCommand> targets;

    private PayType payType;

    private CreatePayCommand(Long payCreatorId, Money totalAmount, Bank bank, List<TargetOfCreatePayCommand> targets, PayType payType) {
        this.payCreatorId = payCreatorId;
        this.totalAmount = totalAmount;
        this.bank = bank;
        this.targets = targets;
        this.payType = payType;
    }

    public static CreatePayCommand create(Long payCreatorId, int totalAmount, String bankName, String bankAccountNum, List<TargetOfPayRequest> targets, String valueOfPayType) {
        return new CreatePayCommand(
                payCreatorId,
                Money.of(totalAmount),
                Bank.of(bankName, bankAccountNum),
                mapToInputModel(targets),
                PayType.fromString(valueOfPayType)
        );
    }

    private static List<TargetOfCreatePayCommand> mapToInputModel(List<TargetOfPayRequest> targets) {
        List<TargetOfCreatePayCommand> result = new ArrayList<>();
        for (TargetOfPayRequest target : targets) {
            result.add(TargetOfCreatePayCommand.create(target.getTargetMemberId(), target.getRequestedAmount()));
        }
        return result;
    }



}
