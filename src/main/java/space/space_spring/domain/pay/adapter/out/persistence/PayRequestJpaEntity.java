package space.space_spring.domain.pay.adapter.out.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Pay_Request")
public class PayRequestJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "pay_request_id")
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_member_id")
    @NotNull
    private SpaceMemberJpaEntity payCreator;

    @Column(name = "total_amount")
    @NotNull
    private int totalAmount;

    @Column(name = "received_amount")
    @NotNull
    private int receivedAmount;

    @Column(name = "totol_target_num")
    @NotNull
    private int totalTargetNum;

    @Column(name = "send_complete_target_num")
    @NotNull
    private int sendCompleteTargetNum;

    @Column(name = "bank_name")
    @NotNull
    private String bankName;

    @Column(name = "bank_account_num")
    @NotNull
    private String bankAccountNum;

    @Column(name = "is_complete")
    @NotNull
    private boolean isComplete;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type")
    @NotNull
    private PayType payType;

    @Builder
    private PayRequestJpaEntity(SpaceMemberJpaEntity payCreator, int totalAmount, int receivedAmount, int totalTargetNum, int sendCompleteTargetNum, String bankName, String bankAccountNum, boolean isComplete, PayType payType) {
        this.payCreator = payCreator;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.totalTargetNum = totalTargetNum;
        this.sendCompleteTargetNum = sendCompleteTargetNum;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.isComplete = isComplete;
        this.payType = payType;
    }

    public static PayRequestJpaEntity createNewJpaEntity(SpaceMemberJpaEntity payCreator, int totalAmount, int totalTargetNum, String bankName, String bankAccountNum, PayType payType) {
        return PayRequestJpaEntity.builder()
                .payCreator(payCreator)
                .totalAmount(totalAmount)
                .receivedAmount(0)
                .totalTargetNum(totalTargetNum)
                .sendCompleteTargetNum(0)
                .bankName(bankName)
                .bankAccountNum(bankAccountNum)
                .isComplete(false)
                .payType(payType)
                .build();
    }

    public static PayRequestJpaEntity of(SpaceMemberJpaEntity payCreator, int totalAmount, int receivedAmount, int totalTargetNum, int sendCompleteTargetNum, String bankName, String bankAccountNum, boolean isComplete, PayType payType) {
        return PayRequestJpaEntity.builder()
                .payCreator(payCreator)
                .totalAmount(totalAmount)
                .receivedAmount(receivedAmount)
                .totalTargetNum(totalTargetNum)
                .sendCompleteTargetNum(sendCompleteTargetNum)
                .bankName(bankName)
                .bankAccountNum(bankAccountNum)
                .isComplete(isComplete)
                .payType(payType)
                .build();
    }

}
