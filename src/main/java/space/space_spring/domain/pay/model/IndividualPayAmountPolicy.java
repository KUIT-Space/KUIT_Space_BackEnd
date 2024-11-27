package space.space_spring.domain.pay.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import space.space_spring.exception.CustomException;

import java.util.List;

import static space.space_spring.response.status.BaseExceptionResponseStatus.INVALID_INDIVIDUAL_AMOUNT;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndividualPayAmountPolicy implements PayAmountPolicy {

    @Override
    public void validatePayAmount(int totalAmount, List<Integer> targetAmounts) {
        int sumOfTargetAmounts = targetAmounts.stream().mapToInt(Integer::intValue).sum();

        if (sumOfTargetAmounts != totalAmount) {
            throw new CustomException(INVALID_INDIVIDUAL_AMOUNT);
        }
    }
}
