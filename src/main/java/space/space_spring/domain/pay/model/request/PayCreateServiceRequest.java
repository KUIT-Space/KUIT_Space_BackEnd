package space.space_spring.domain.pay.model.request;

import com.amazonaws.services.ec2.model.UpdateSecurityGroupRuleDescriptionsIngressRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.model.PayType;

import java.util.List;

@Getter
@NoArgsConstructor
public class PayCreateServiceRequest {

    private int totalAmount;

    private String bankName;

    private String bankAccountNum;

    private List<PayCreateTargetInfo> payCreateTargetInfos;

    private PayType payType;

    @Builder
    private PayCreateServiceRequest(int totalAmount, String bankName, String bankAccountNum, List<PayCreateTargetInfo> payCreateTargetInfos, PayType payType) {
        this.totalAmount = totalAmount;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.payCreateTargetInfos = payCreateTargetInfos;
        this.payType = payType;
    }

}
