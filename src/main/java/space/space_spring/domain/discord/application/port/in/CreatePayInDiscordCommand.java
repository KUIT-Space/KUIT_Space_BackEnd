package space.space_spring.domain.discord.application.port.in;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

@Getter
public class CreatePayInDiscordCommand {

    private Long payCreatorId;

    private Money totalAmount;

    private Bank bank;

    private List<TargetOfCreatePayInDiscordCommand> targets;

    private NaturalNumber totalTargetNum;

    private PayType payType;

    @Builder
    public CreatePayInDiscordCommand(Long payCreatorId, Money totalAmount, Bank bank, List<TargetOfCreatePayInDiscordCommand> targets, NaturalNumber totalTargetNum, PayType payType) {
        this.payCreatorId = payCreatorId;
        this.totalAmount = totalAmount;
        this.bank = bank;
        this.targets = targets;
        this.totalTargetNum = totalTargetNum;
        this.payType = payType;
    }

}
