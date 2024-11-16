package space.space_spring.domain.pay.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.model.dto.PayTargetInfoDto;
import space.space_spring.entity.BaseEntity;

@Entity
@Getter
@Table(name = "Pay_Request_Target")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
                                            // -> @OneToOne 어노테이션으로 User 엔티티 가지고 있는 방향 생각해볼것

    @Column(name = "request_amount")
    private int requestedAmount;              // 정산 요청받은 금액

    @Column(name = "is_complete")
    private boolean isComplete;


    @Builder
    private PayRequestTarget(PayRequest payRequest, Long targetUserId, int requestedAmount) {
        this.payRequest = payRequest;
        this.targetUserId = targetUserId;
        this.requestedAmount = requestedAmount;
        this.isComplete = false;
    }

    public static PayRequestTarget create(PayRequest payRequest, Long targetUserId, int requestedAmount) {
        PayRequestTarget build = PayRequestTarget.builder()
                .payRequest(payRequest)
                .targetUserId(targetUserId)
                .requestedAmount(requestedAmount)
                .build();

        payRequest.addPayRequestTarget(build);       // 연관관계 편의 메서드

        return build;
    }

    public void changeCompleteStatus(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public PayTargetInfoDto createPayTargetInfo() {
        return PayTargetInfoDto.builder()
                .payRequestTargetId(payRequestTargetId)
                .payCreatorName(payRequest.getPayCreateUser().getUserName())
                .requestedAmount(requestedAmount)
                .bankName(payRequest.getBankName())
                .bankAccountNum(payRequest.getBankAccountNum())
                .build();
    }
}
