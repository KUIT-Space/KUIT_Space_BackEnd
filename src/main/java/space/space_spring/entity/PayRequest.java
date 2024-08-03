package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;

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

    @Column(name = "is_complete")
    private boolean isComplete;

    public void savePayRequest(User payCreateUser, Space space, int totalAmount, String bankName, String bankAccountNum, boolean isComplete) {
        this.payCreateUser = payCreateUser;
        this.space = space;
        this.totalAmount = totalAmount;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.isComplete = isComplete;
    }

    public void changeCompleteStatus(boolean isComplete) {
        this.isComplete = isComplete;
    }
}
