package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PayRequestTarget extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "pay_request_target_id")
    private Long payRequestTargetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_request_id")
    private PayRequest payRequest;

    @Column(name = "target_user_id")
    private Long targetUserId;              // User 객체를 필드로 가지는 것보다 이게 더 낫나??

    @Column(name = "request_amount")
    private int requestAmount;

    @Column(name = "is_complete")
    private boolean isComplete;

    public void savePayRequestTarget(PayRequest payRequest, Long targetUserId, int requestAmount, boolean isComplete) {
        this.payRequest = payRequest;
        this.targetUserId = targetUserId;
        this.requestAmount = requestAmount;
        this.isComplete = isComplete;
    }
}
