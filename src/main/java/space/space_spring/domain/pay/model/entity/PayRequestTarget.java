package space.space_spring.domain.pay.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import space.space_spring.domain.pay.model.dto.PayReceiveInfoDto;
import space.space_spring.entity.BaseEntity;

@Entity
@Getter
@Table(name = "Pay_Request_Target")
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
    private int requestAmount;

    @Column(name = "is_complete")
    private boolean isComplete;

    public void savePayRequestTarget(PayRequest payRequest, Long targetUserId, int requestAmount, boolean isComplete) {
        this.payRequest = payRequest;
        this.targetUserId = targetUserId;
        this.requestAmount = requestAmount;
        this.isComplete = isComplete;
    }

    public void changeCompleteStatus(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public PayReceiveInfoDto createPayReceiveInfo() {
        String payCreatorName = payRequest.getPayCreateUser().getUserName();          // 리펙토링 필요
        int requestAmount = this.requestAmount;

        // 정산 생성자가 요청한 은행 정보도 response에 추가
        String bankName = payRequest.getBankName();
        String bankAccountNum = payRequest.getBankAccountNum();

        return new PayReceiveInfoDto(this.payRequestTargetId, payCreatorName, requestAmount, bankName, bankAccountNum);
    }
}
