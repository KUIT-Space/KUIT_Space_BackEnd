package space.space_spring.domain.pay.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayRequestJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private SpaceMemberJpaEntity payCreator;

    private int totalAmount;

    private String bankName;

    private String bankAccountNum;

    private int receivedAmount;

    private boolean isComplete;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Builder
    private PayRequestJpaEntity(SpaceMemberJpaEntity payCreator, int totalAmount, String bankName, String bankAccountNum, PayType payType) {
        this.payCreator = payCreator;
        this.totalAmount = totalAmount;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.receivedAmount = 0;
        this.isComplete = false;
        this.payType = payType;
    }

    public static PayRequestJpaEntity create(SpaceMemberJpaEntity payCreator, int totalAmount, String bankName, String bankAccountNum, PayType payType) {
        return PayRequestJpaEntity.builder()
                .payCreator(payCreator)
                .totalAmount(totalAmount)
                .bankName(bankName)
                .bankAccountNum(bankAccountNum)
                .payType(payType)
                .build();
    }

}
