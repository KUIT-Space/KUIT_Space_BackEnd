package space.space_spring.domain.pay.model.firstCollection;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.pay.model.dto.PayTargetInfoDto;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;

import java.util.List;

@Getter
public class PayRequestTargets {

    private final List<PayRequestTarget> payRequestTargets;

    @Builder
    private PayRequestTargets(List<PayRequestTarget> payRequestTargets) {
        this.payRequestTargets = payRequestTargets;
    }

    public static PayRequestTargets create(List<PayRequestTarget> payRequestTargets) {
        return PayRequestTargets.builder()
                .payRequestTargets(payRequestTargets)
                .build();
    }

    public PayTargetInfos getPayTargetInfos() {
        PayTargetInfos payTargetInfos = new PayTargetInfos();

        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            PayTargetInfoDto payTargetInfo = payRequestTarget.createPayTargetInfo();
            payTargetInfos.add(payTargetInfo);
        }

        return payTargetInfos;
    }


}
