package space.space_spring.domain.pay.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PayCreateValidator {

    public void validatePayAmount(PayType payType, int totalAmount, List<PayCreateTargetInfo> payCreateTargetInfos) {
        List<Integer> targetAmounts = new ArrayList<>();
        for (PayCreateTargetInfo payCreateTargetInfo : payCreateTargetInfos) {
            targetAmounts.add(payCreateTargetInfo.getRequestedAmount());
        }

        payType.getPayAmountPolicy().validatePayAmount(totalAmount, targetAmounts);
    }
}
