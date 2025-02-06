package space.space_spring.domain.pay.domain;

import lombok.Getter;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

@Getter
public class PayRequestTarget {

    private SpaceMember targetMember;           // 이거도 jpa엔티티여서 도메인 엔티티로 변경해야 함

    private PayRequest payRequest;

    private Money requestedAmount;

    private boolean isComplete;

    private PayRequestTarget(SpaceMember targetMember, PayRequest payRequest, Money requestedAmount) {
        this.targetMember = targetMember;
        this.payRequest = payRequest;
        this.requestedAmount = requestedAmount;
        this.isComplete = false;
    }

    public static PayRequestTarget create(SpaceMember targetMember, PayRequest payRequest, Money requestedAmount) {
        return new PayRequestTarget(targetMember, payRequest, requestedAmount);
    }
}
