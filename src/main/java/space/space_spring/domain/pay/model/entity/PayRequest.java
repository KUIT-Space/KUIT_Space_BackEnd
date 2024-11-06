package space.space_spring.domain.pay.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.entity.BaseEntity;
import space.space_spring.entity.Space;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "Pay_Request")
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
    private int totalAmount;             // 정산의 총 금액

    @Column(name = "pay_request_bank")
    private String bankName;            // 정산 받을 은행 이름

    @Column(name = "bank_account_num")
    private String bankAccountNum;      // 정산 받을 은행 계좌번호

    @Column(name = "un_requested_amount")
    private int unRequestedAmount;      // 미정산 금액

    @Column(name = "is_complete")
    private boolean isComplete;

    @OneToMany(mappedBy = "payRequest")
    private List<PayRequestTarget> payRequestTargets = new ArrayList<>();

    public void savePayRequest(User payCreateUser, Space space, int totalAmount, String bankName, String bankAccountNum, int unRequestedAmount, boolean isComplete) {
        this.payCreateUser = payCreateUser;
        this.space = space;
        this.totalAmount = totalAmount;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.unRequestedAmount = unRequestedAmount;
        this.isComplete = isComplete;
    }

    public void changeCompleteStatus(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public PayRequestInfoDto createPayRequestInfo() {
        int totalAmount = this.totalAmount;
        int receiveAmount = 0;
        int totalTargetNum = 0;
        int receiveTargetNum = 0;

        for (PayRequestTarget payRequestTarget : this.payRequestTargets) {
            if (payRequestTarget.isComplete()) {
                // 해당 타겟이 돈을 낸 경우
                receiveAmount += payRequestTarget.getRequestAmount();
                receiveTargetNum++;
            }

            totalTargetNum++;
        }

        return new PayRequestInfoDto(
                this.payRequestId, totalAmount, receiveAmount, totalTargetNum, receiveTargetNum
        );
    }
}
