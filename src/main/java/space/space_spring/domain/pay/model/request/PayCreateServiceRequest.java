package space.space_spring.domain.pay.model.request;

import com.amazonaws.services.ec2.model.UpdateSecurityGroupRuleDescriptionsIngressRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PayCreateServiceRequest {

    private int totalAmount;

    private String bankName;

    private String bankAccountNum;

    private List<TargetInfo> targetInfos;

    @Getter
    @NoArgsConstructor
    public static class TargetInfo {
        private Long targetUserId;

        private int requestedAmount;

        @Builder
        private TargetInfo(Long targetUserId, Integer requestedAmount) {
            this.targetUserId = targetUserId;
            this.requestedAmount = requestedAmount;
        }
    }

    @Builder
    private PayCreateServiceRequest(int totalAmount, String bankName, String bankAccountNum, List<TargetInfo> targetInfos) {
        this.totalAmount = totalAmount;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.targetInfos = targetInfos;
    }

}
