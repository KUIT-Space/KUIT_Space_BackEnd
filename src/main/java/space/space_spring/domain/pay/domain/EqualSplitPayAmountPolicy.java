package space.space_spring.domain.pay.domain;

import space.space_spring.global.exception.CustomException;

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_EQUAL_SPLIT_AMOUNT;

public class EqualSplitPayAmountPolicy implements PayAmountPolicy {

    @Override
    public void validatePayAmount(Money totalAMount, List<Money> targetAmounts) {
        int numberOfTarget = targetAmounts.size();
        Money expectedAmountPerTarget = totalAMount.dividedBy(numberOfTarget);

        for (Money targetAmount : targetAmounts) {
            if (!targetAmount.isEqual(expectedAmountPerTarget)) {
                throw new CustomException(INVALID_EQUAL_SPLIT_AMOUNT);
            }
        }
    }
}
