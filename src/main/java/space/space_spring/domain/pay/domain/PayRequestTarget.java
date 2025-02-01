package space.space_spring.domain.pay.domain;

import lombok.Getter;
import space.space_spring.domain.spaceMember.SpaceMember;

@Getter
public class PayRequestTarget {

    private Long id;

    private SpaceMember targetMember;           // 이거도 jpa엔티티여서 도메인 엔티티로 변경해야 함

    private PayRequest payRequest;

    private Money requestedAmount;

    private boolean isComplete;

    private PayRequestTarget(Long id, SpaceMember targetMember, PayRequest payRequest, Money requestedAmount) {
        this.id = id;
        this.targetMember = targetMember;
        this.payRequest = payRequest;
        this.requestedAmount = requestedAmount;
        this.isComplete = false;
    }

    public static PayRequestTarget create(Long id, SpaceMember targetMember, PayRequest payRequest, Money requestedAmount) {
        return new PayRequestTarget(id, targetMember, payRequest, requestedAmount);
    }

    public boolean isComplete() {
        return isComplete;
    }
}
