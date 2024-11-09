package space.space_spring.domain.pay.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.entity.BaseEntity;
import space.space_spring.domain.space.model.entity.Space;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "Pay_Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayRequest extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "pay_request_id")
    private Long payRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User payCreateUser;         // 정산을 시작한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;                // 정산이 이루어지는 스페이스

    @Column(name = "total_amount")
    private int totalAmount;            // 정산의 총 금액

    @Column(name = "pay_request_bank")
    private String bankName;            // 정산 받을 은행 이름

    @Column(name = "bank_account_num")
    private String bankAccountNum;      // 정산 받을 은행 계좌번호

    @Column(name = "received_amount")
    private int receivedAmount;         // 정산 받은 금액

    @Column(name = "is_complete")
    private boolean isComplete;


    public void changeCompleteStatus(boolean isComplete) {
        this.isComplete = isComplete;
    }

    @Builder
    private PayRequest(User payCreateUser, Space space, int totalAmount, String bankName, String bankAccountNum) {
        this.payCreateUser = payCreateUser;
        this.space = space;
        this.totalAmount = totalAmount;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.receivedAmount = 0;
        this.isComplete = false;
    }

    public static PayRequest create(User payCreateUser, Space space, int totalAmount, String bankName, String bankAccountNum) {
        return PayRequest.builder()
                .payCreateUser(payCreateUser)
                .space(space)
                .totalAmount(totalAmount)
                .bankName(bankName)
                .bankAccountNum(bankAccountNum)
                .build();
    }

    public PayRequestInfoDto createPayRequestInfo(List<PayRequestTarget> payRequestTargets) {
        int totalTargetNum = 0;
        int paySendTargetNum = 0;

        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            if (payRequestTarget.isComplete()) {
                // 해당 타겟이 돈을 낸 경우
                paySendTargetNum++;
            }

            totalTargetNum++;
        }

        // 빌더 패턴으로 변경
        return new PayRequestInfoDto(
                payRequestId, totalAmount, receivedAmount, totalTargetNum, paySendTargetNum
        );
    }
}
