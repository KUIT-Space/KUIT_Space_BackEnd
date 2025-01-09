package space.space_spring.domain.pay.model.firstCollection;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;
import space.space_spring.domain.pay.model.entity.PayRequest;

import java.util.List;

@Getter
public class PayRequests {

    private final List<PayRequest> payRequests;

    @Builder
    private PayRequests(List<PayRequest> payRequests) {
        this.payRequests = payRequests;
    }

    public static PayRequests create(List<PayRequest> payRequests) {
        return PayRequests.builder()
                .payRequests(payRequests)
                .build();
    }

    public PayRequestInfos getPayRequestInfos() {
        PayRequestInfos payRequestInfos = new PayRequestInfos();

        for (PayRequest payRequest : payRequests) {
            PayRequestInfoDto payRequestInfo = payRequest.getPayRequestInfo();
            payRequestInfos.add(payRequestInfo);
        }

        return payRequestInfos;
    }



}
