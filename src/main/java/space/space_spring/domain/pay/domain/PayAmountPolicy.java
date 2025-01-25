package space.space_spring.domain.pay.domain;

import java.util.List;

public interface PayAmountPolicy {

    void validatePayAmount(Money totalAMount, List<Money> targetAmounts);

}
