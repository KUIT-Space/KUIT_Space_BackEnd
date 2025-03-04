package space.space_spring.domain.pay.domain;

import lombok.Getter;
import space.space_spring.global.common.entity.BaseInfo;
import space.space_spring.global.common.enumStatus.BaseStatusType;

@Getter
public class PayRequestTarget {

    private Long id;

    private Long targetMemberId;

    private Long payRequestId;

    private Money requestedAmount;

    private boolean isComplete;

    private BaseInfo baseInfo;

    private PayRequestTarget(Long id, Long targetMemberId, Long payRequestId, Money requestedAmount, boolean isComplete,
                             BaseInfo baseInfo) {
        this.id = id;
        this.targetMemberId = targetMemberId;
        this.payRequestId = payRequestId;
        this.requestedAmount = requestedAmount;
        this.isComplete = isComplete;
        this.baseInfo = baseInfo;
    }

    public static PayRequestTarget create(Long id, Long targetMemberId, Long payRequestId, Money requestedAmount, BaseInfo baseInfo) {
        return new PayRequestTarget(id, targetMemberId, payRequestId, requestedAmount, false, baseInfo);
    }

    /**
     * 처음 Domain Entity 생성 시 사용하는 정적 펙토리 메서드
     */
    public static PayRequestTarget withoutId(Long targetMemberId, Long payRequestId, Money requestedAmount) {
        return new PayRequestTarget(null, targetMemberId, payRequestId, requestedAmount, false, BaseInfo.ofEmpty());
    }

    public static PayRequestTarget of(Long id, Long targetMemberId, Long payRequestId, Money requestedAmount, boolean isComplete, BaseInfo baseInfo) {
        return new PayRequestTarget(id, targetMemberId, payRequestId, requestedAmount, isComplete, baseInfo);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public boolean isSameTargetMember(Long targetMemberId) {
        return this.targetMemberId.equals(targetMemberId);
    }

    public void completeForRequestedPay() {
        this.isComplete = true;
    }

    public void changeToActive() {
        this.baseInfo.changeStatus(BaseStatusType.ACTIVE);
    }

    public void changeToInactive() {
        this.baseInfo.changeStatus(BaseStatusType.INACTIVE);
    }
}
