package space.space_spring.domain.pay.model;

import space.space_spring.exception.CustomException;

import java.util.List;

import static space.space_spring.response.status.BaseExceptionResponseStatus.INVALID_EQUAL_SPLIT_AMOUNT;

public class EqualSplitPayAmountPolicy implements PayAmountPolicy {

    @Override
    public void validatePayAmount(int totalAmount, List<Integer> targetAmounts) {
        int numberOfTarget = targetAmounts.size();
        int expectedAmountPerTarget = totalAmount / numberOfTarget;

        for (Integer targetAmount : targetAmounts) {
            if (targetAmount != expectedAmountPerTarget) {
                throw new CustomException(INVALID_EQUAL_SPLIT_AMOUNT);
            }
        }
    }
}
