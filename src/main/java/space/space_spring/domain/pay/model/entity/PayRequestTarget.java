package space.space_spring.domain.pay.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.model.dto.PayReceiveInfoDto;
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
        return PayRequestTarget.builder()
                .payRequest(payRequest)
                .targetUserId(targetUserId)
                .requestedAmount(requestedAmount)
                .build();
    }

    public void changeCompleteStatus(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public PayReceiveInfoDto createPayReceiveInfo() {
        String payCreatorName = payRequest.getPayCreateUser().getUserName();            // 이정도는 괜찮지 않나?
        int requestedAmount = this.requestedAmount;

        // 정산 생성자가 요청한 은행 정보도 response에 추가
        String bankName = payRequest.getBankName();
        String bankAccountNum = payRequest.getBankAccountNum();

        // 빌더패턴으로 변경
        return new PayReceiveInfoDto(payRequestTargetId, payCreatorName, requestedAmount, bankName, bankAccountNum);
    }
}
