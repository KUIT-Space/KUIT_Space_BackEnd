package space.space_spring.domain.pay.model.firstCollection;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.model.dto.PayTargetInfoDto;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;

import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayRequestTargets {

    @OneToMany(mappedBy = "payRequest")
    private List<PayRequestTarget> payRequestTargets;

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
            PayTargetInfoDto payTargetInfo = payRequestTarget.getPayTargetInfo();
            payTargetInfos.add(payTargetInfo);
        }

        return payTargetInfos;
    }

    public void addPayRequestTarget(PayRequestTarget payRequestTarget) {
        payRequestTargets.add(payRequestTarget);
    }

    public int countTotalTargets() {
        return payRequestTargets.size();
    }

    public int countCompleteTargets() {
        return (int) payRequestTargets.stream()
                .filter(PayRequestTarget::isComplete)
                .count();
    }
}
