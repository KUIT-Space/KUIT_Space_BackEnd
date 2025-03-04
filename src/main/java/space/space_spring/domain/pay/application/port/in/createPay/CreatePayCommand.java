package space.space_spring.domain.pay.application.port.in.createPay;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.domain.pay.adapter.in.web.createPay.TargetOfPayRequest;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.util.NaturalNumber;

import java.time.LocalDateTime;
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

    private NaturalNumber totalTargetNum;

    private PayType payType;

    @Builder
    public CreatePayCommand(Long payCreatorId, int totalAmount, String bankName, String bankAccountNum, List<TargetOfPayRequest> targets, String valueOfPayType) {
        this.payCreatorId = payCreatorId;
        this.totalAmount = Money.of(totalAmount);
        this.bank = Bank.of(bankName, bankAccountNum);
        this.targets = mapToInputModel(targets);
        this.totalTargetNum = NaturalNumber.of(targets.size());
        this.payType = PayType.fromString(valueOfPayType);
    }

    private static List<TargetOfCreatePayCommand> mapToInputModel(List<TargetOfPayRequest> targets) {
        List<TargetOfCreatePayCommand> result = new ArrayList<>();
        for (TargetOfPayRequest target : targets) {
            result.add(TargetOfCreatePayCommand.create(target.getTargetMemberId(), target.getRequestedAmount()));
        }
        return result;
    }

    public PayRequest toDomainEntity(Long discordMessageId) {
        return PayRequest.withoutId(payCreatorId, discordMessageId, totalAmount, totalTargetNum, bank, payType);
    }
}
