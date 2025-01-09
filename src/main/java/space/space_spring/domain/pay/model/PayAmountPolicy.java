package space.space_spring.domain.pay.model;

import java.util.List;

public interface PayAmountPolicy {

    void validatePayAmount(int totalAmount, List<Integer> targetAmounts);
}
