package space.space_spring.domain.pay.domain;

import lombok.Getter;

@Getter
public enum PayType {
    /**
     * INDIVIDUAL : 정산 요청 금액을 직접 입력
     * EQUAL_SPLIT : 정산 요청 금액을 1/n 분배
     */

    INDIVIDUAL(new IndividualPayAmountPolicy()),
    EQUAL_SPLIT(new EqualSplitPayAmountPolicy());

    private final PayAmountPolicy payAmountPolicy;

    PayType(PayAmountPolicy payAmountPolicy) {
        this.payAmountPolicy = payAmountPolicy;
    }

}
