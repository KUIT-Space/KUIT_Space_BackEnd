package space.space_spring.domain.pay.adapter.out.persistence;

import jakarta.persistence.*;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_member_id")
    private SpaceMemberJpaEntity payCreator;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "totol_target_num")
    private int totalTargetNum;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_num")
    private String bankAccountNum;

    @Column(name = "received_amount")
    private int receivedAmount;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type")
    private PayType payType;

    @Builder
    private PayRequestJpaEntity(SpaceMemberJpaEntity payCreator, int totalAmount, int totalTargetNum, String bankName, String bankAccountNum, PayType payType) {
        this.payCreator = payCreator;
        this.totalAmount = totalAmount;
        this.totalTargetNum = totalTargetNum;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.receivedAmount = 0;
        this.isComplete = false;
        this.payType = payType;
    }

    public static PayRequestJpaEntity create(SpaceMemberJpaEntity payCreator, int totalAmount, int totalTargetNum, String bankName, String bankAccountNum, PayType payType) {
        return PayRequestJpaEntity.builder()
                .payCreator(payCreator)
                .totalAmount(totalAmount)
                .totalTargetNum(totalTargetNum)
                .bankName(bankName)
                .bankAccountNum(bankAccountNum)
                .payType(payType)
                .build();
    }

}
