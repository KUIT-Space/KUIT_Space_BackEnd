package space.space_spring.domain.pay.domain;

import lombok.Getter;

import space.space_spring.global.common.entity.BaseInfo;
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

    private BaseInfo baseInfo;

    private PayRequest(Long id, Long payCreatorId, Long discordMessageId, Money totalAmount, NaturalNumber totalTargetNum, Bank bank, PayType payType,
                       BaseInfo baseInfo) {
        this.id = id;
        this.payCreatorId = payCreatorId;
        this.discordMessageId = discordMessageId;
        this.totalAmount = totalAmount;
        this.totalTargetNum = totalTargetNum;
        this.bank = bank;
        this.payType = payType;
        this.baseInfo = baseInfo;
    }

    public static PayRequest create(Long id, Long payCreatorId, Long discordMessageId, Money totalAmount, NaturalNumber totalTargetNum, Bank bank, PayType payType,
                                    BaseInfo baseInfo) {
        return new PayRequest(id, payCreatorId, discordMessageId, totalAmount, totalTargetNum, bank, payType, baseInfo);
    }

    /**
     * 처음 Domain Entity 생성 시 사용하는 정적 펙토리 메서드
     */
    public static PayRequest withoutId(Long payCreatorId, Long discordMessageId, Money totalAmount, NaturalNumber totalTargetNum, Bank bank, PayType payType) {
        return new PayRequest(null, payCreatorId, discordMessageId, totalAmount, totalTargetNum, bank, payType, BaseInfo.ofEmpty());
    }

    public boolean isEqualToTotalAmount(Money money) {
        return totalAmount.equals(money);
    }

    public boolean isEqualToTotalTargetNum(NaturalNumber number) {
        return totalTargetNum.equals(number);
    }

    public boolean isPayCreator(Long spaceMemberId) {
        return payCreatorId.equals(spaceMemberId);
    }
}
