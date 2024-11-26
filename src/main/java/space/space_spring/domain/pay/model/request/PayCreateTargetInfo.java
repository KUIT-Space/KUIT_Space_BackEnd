package space.space_spring.domain.pay.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PayCreateTargetInfo {

    private Long targetUserId;

    private int requestedAmount;

    @Builder
    private PayCreateTargetInfo(Long targetUserId, int requestedAmount) {
        this.targetUserId = targetUserId;
        this.requestedAmount = requestedAmount;
    }
}
