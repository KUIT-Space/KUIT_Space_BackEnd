package space.space_spring.domain.pay.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_EQUAL_SPLIT_AMOUNT;

class EqualSplitPayAmountPolicyTest {

    @Test
    @DisplayName("전체 정산 금액을 1/N 한 금액과 정산 타겟들이 각각 요청받은 금액이 일치하는지 검증한다.")
    void validatePayAmount1() throws Exception {
        //given
        int totalAmount = 40000;
        int requestedAmount1 = 10000;
        int requestedAmount2 = 10000;
        int requestedAmount3 = 10000;
        int requestedAmount4 = 10000;
        List<Money> targetAmounts = List.of(Money.of(requestedAmount1), Money.of(requestedAmount2), Money.of(requestedAmount3), Money.of(requestedAmount4));

        EqualSplitPayAmountPolicy equalSplitPayAmountPolicy = new EqualSplitPayAmountPolicy();

        //when //then
        assertDoesNotThrow(() -> equalSplitPayAmountPolicy.validatePayAmount(Money.of(totalAmount), targetAmounts));
    }

    @Test
    @DisplayName("전체 정산 금액이 나누어 떨어지지 않는 경우, 정수의 나눗셈 계산을 통해 얻은 값을 정산 타겟들에게 요청한 금액으로 간주한다.")
    void validatePayAmount2() throws Exception {
        //given
        int totalAmount = 10000;
        int requestedAmount1 = 3333;
        int requestedAmount2 = 3333;
        int requestedAmount3 = 3333;
        List<Money> targetAmounts = List.of(Money.of(requestedAmount1), Money.of(requestedAmount2), Money.of(requestedAmount3));

        EqualSplitPayAmountPolicy equalSplitPayAmountPolicy = new EqualSplitPayAmountPolicy();

        //when //then
        assertDoesNotThrow(() -> equalSplitPayAmountPolicy.validatePayAmount(Money.of(totalAmount), targetAmounts));
    }

    @Test
    @DisplayName("전체 정산 금액을 1/N 한 금액과 정산 타겟들이 각각 요청받은 금액이 일치하지 않을 경우, 예외를 발생시킨다.")
    void validatePayAmount3() throws Exception {
        //given
        int totalAmount = 40000;
        int requestedAmount1 = 10000;
        int requestedAmount2 = 10000;
        int requestedAmount3 = 10000;
        int requestedAmount4 = 15000;       // 10000원이 아님
        List<Money> targetAmounts = List.of(Money.of(requestedAmount1), Money.of(requestedAmount2), Money.of(requestedAmount3), Money.of(requestedAmount4));

        EqualSplitPayAmountPolicy equalSplitPayAmountPolicy = new EqualSplitPayAmountPolicy();

        //when //then
        assertThatThrownBy(() -> equalSplitPayAmountPolicy.validatePayAmount(Money.of(totalAmount), targetAmounts))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_EQUAL_SPLIT_AMOUNT.getMessage());
    }

}