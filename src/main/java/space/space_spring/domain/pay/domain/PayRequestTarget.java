package space.space_spring.domain.pay.domain;

import lombok.Getter;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

@Getter
public class PayRequestTarget {

    private Long id;

    private Long targetMemberId;

    private Long payRequestId;

    private Money requestedAmount;

    private boolean isComplete;

    private PayRequestTarget(Long id, Long targetMemberId, Long payRequestId, Money requestedAmount, boolean isComplete) {
        this.id = id;
        this.targetMemberId = targetMemberId;
        this.payRequestId = payRequestId;
        this.requestedAmount = requestedAmount;
        this.isComplete = isComplete;
    }

    public static PayRequestTarget create(Long id, Long targetMemberId, Long payRequestId, Money requestedAmount) {
        return new PayRequestTarget(id, targetMemberId, payRequestId, requestedAmount, false);
    }

    public static PayRequestTarget withoutId(Long targetMemberId, Long payRequestId, Money requestedAmount) {
        return new PayRequestTarget(null, targetMemberId, payRequestId, requestedAmount, false);
    }

    public static PayRequestTarget of(Long id, Long targetMemberId, Long payRequestId, Money requestedAmount, boolean isComplete) {
        return new PayRequestTarget(id, targetMemberId, payRequestId, requestedAmount, isComplete);
    }

    public boolean isComplete() {
        return isComplete;
    }
}
