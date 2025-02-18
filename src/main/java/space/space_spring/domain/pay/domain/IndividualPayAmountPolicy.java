package space.space_spring.domain.pay.domain;

import space.space_spring.global.exception.CustomException;

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_INDIVIDUAL_AMOUNT;

public class IndividualPayAmountPolicy implements PayAmountPolicy {

    @Override
    public void validatePayAmount(Money totalAMount, List<Money> targetAmounts) {
        Money sumOfTargetAmounts = Money.of(0);
        for (Money targetAmount : targetAmounts) {
            sumOfTargetAmounts = sumOfTargetAmounts.add(targetAmount);
        }

        if (!totalAMount.equals(sumOfTargetAmounts)) {
            throw new CustomException(INVALID_INDIVIDUAL_AMOUNT);
        }
    }
}
