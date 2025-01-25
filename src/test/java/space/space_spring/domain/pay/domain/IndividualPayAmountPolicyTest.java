package space.space_spring.domain.pay.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_INDIVIDUAL_AMOUNT;

class IndividualPayAmountPolicyTest {

    @Test
    @DisplayName("전체 정산 금액과 정산 타겟들이 요청받은 금액들의 합이 일치하는지 검증한다.")
    void validatePayAmount1() throws Exception {
        //given
        int totalAmount = 40000;
        int requestedAmount1 = 16000;
        int requestedAmount2 = 15000;
        int requestedAmount3 = 5000;
        int requestedAmount4 = 4000;
        List<Money> targetAmounts = List.of(Money.of(requestedAmount1), Money.of(requestedAmount2), Money.of(requestedAmount3), Money.of(requestedAmount4));

        IndividualPayAmountPolicy individualPayAmountPolicy = new IndividualPayAmountPolicy();

        //when //then
        assertDoesNotThrow(() -> individualPayAmountPolicy.validatePayAmount(Money.of(totalAmount), targetAmounts));
    }

    @Test
    @DisplayName("전체 정산 금액과 정산 타겟들이 요청받은 금액들의 합이 일치하지 않을 경우, 예외를 발생시킨다.")
    void validatePayAmount2() throws Exception {
        //given
        int totalAmount = 40000;
        int requestedAmount1 = 16000;
        int requestedAmount2 = 15000;
        int requestedAmount3 = 5000;
        int requestedAmount4 = 1000;        // 합은 37000 원
        List<Money> targetAmounts = List.of(Money.of(requestedAmount1), Money.of(requestedAmount2), Money.of(requestedAmount3), Money.of(requestedAmount4));

        IndividualPayAmountPolicy individualPayAmountPolicy = new IndividualPayAmountPolicy();

        //when //then
        assertThatThrownBy(() -> individualPayAmountPolicy.validatePayAmount(Money.of(totalAmount), targetAmounts))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_INDIVIDUAL_AMOUNT.getMessage());
    }
}