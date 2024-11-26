package space.space_spring.domain.pay.model;

import org.springframework.stereotype.Component;
import space.space_spring.domain.pay.model.request.PayCreateTargetInfo;

import java.util.List;

@Component
public class PayCreateValidator {

    public void validatePayAmount(int totalAmount, List<PayCreateTargetInfo> payCreateTargetInfos) {

        int sumOfRequestedAmount = 0;
        for (PayCreateTargetInfo payCreateTargetInfo : payCreateTargetInfos) {
            sumOfRequestedAmount += payCreateTargetInfo.getRequestedAmount();
        }

        if (sumOfRequestedAmount != totalAmount) {
            int targetSize = payCreateTargetInfos.size();
            if ((totalAmount - sumOfRequestedAmount) <= targetSize - 1) {

            }
        }

    }
}
