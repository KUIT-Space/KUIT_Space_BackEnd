package space.space_spring.domain.pay.domain;

import lombok.Getter;
import space.space_spring.global.util.NaturalNumber;

@Getter
public class PayRequest {

    private Long id;

    private Long payCreatorId;

    private Long discordMessageId;

    private Money totalAmount;

    private NaturalNumber totalTargetNum;

    private Bank bank;

    private PayType payType;

    private PayRequest(Long id, Long payCreatorId, Long discordMessageId, Money totalAmount, NaturalNumber totalTargetNum, Bank bank, PayType payType) {
        this.id = id;
        this.payCreatorId = payCreatorId;
        this.discordMessageId = discordMessageId;
        this.totalAmount = totalAmount;
        this.totalTargetNum = totalTargetNum;
        this.bank = bank;
        this.payType = payType;
    }

    public static PayRequest create(Long id, Long payCreatorId, Long discordMessageId, Money totalAmount, NaturalNumber totalTargetNum, Bank bank, PayType payType) {
        return new PayRequest(id, payCreatorId, discordMessageId, totalAmount, totalTargetNum, bank, payType);
    }

    public static PayRequest withoutId(Long payCreatorId, Long discordMessageId, Money totalAmount, NaturalNumber totalTargetNum, Bank bank, PayType payType) {
        return new PayRequest(null, payCreatorId, discordMessageId, totalAmount, totalTargetNum, bank, payType);
    }

    public boolean isEqualToTotalAmount(Money money) {
        return totalAmount.equals(money);
    }

    public boolean isEqualToTotalTargetNum(NaturalNumber number) {
        return totalTargetNum.equals(number);
    }

}
