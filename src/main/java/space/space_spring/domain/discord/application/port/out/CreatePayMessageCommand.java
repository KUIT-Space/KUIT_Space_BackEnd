package space.space_spring.domain.discord.application.port.out;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

@Getter
public class CreatePayMessageCommand {

    private Long payCreatorDiscordId;

    private Money totalAmount;

    private Bank bank;

    private List<TargetOfCreatePayMessageCommand> targets;

    private NaturalNumber totalTargetNum;

    private PayType payType;

    @Builder
    public CreatePayMessageCommand(Long payCreatorDiscordId, Money totalAmount, Bank bank, List<TargetOfCreatePayMessageCommand> targets, NaturalNumber totalTargetNum, PayType payType) {
        this.payCreatorDiscordId = payCreatorDiscordId;
        this.totalAmount = totalAmount;
        this.bank = bank;
        this.targets = targets;
        this.totalTargetNum = totalTargetNum;
        this.payType = payType;
    }

}
