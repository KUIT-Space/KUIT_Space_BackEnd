package space.space_spring.domain.pay.domain;

import lombok.Getter;

@Getter
public enum PayType {
    /**
     * INDIVIDUAL : 정산 요청 금액을 직접 입력
     * EQUAL_SPLIT : 정산 요청 금액을 1/n 분배
     */

    /**
     * enum 타입 생성할 때 바로 PayAmountPolicy 객체 생성하는게 맞는건지 다시 생각
     * -> 의존성 주입을 사용하는 방법도 있음
     */

    INDIVIDUAL(new IndividualPayAmountPolicy()),
    EQUAL_SPLIT(new EqualSplitPayAmountPolicy());

    private final PayAmountPolicy payAmountPolicy;

    PayType(PayAmountPolicy payAmountPolicy) {
        this.payAmountPolicy = payAmountPolicy;
    }

    public static PayType fromString(String payTypeString) {
        try {
            return PayType.valueOf(payTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 PayType 입니다. 사용 가능한 값: [INDIVIDUAL, EQUAL_SPLIT], 입력값: " + payTypeString);
        }
    }
}
