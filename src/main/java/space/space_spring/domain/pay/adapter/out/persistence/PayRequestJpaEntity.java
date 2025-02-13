package space.space_spring.domain.pay.adapter.out.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
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

    @Column(name = "discord_message_id")
    @NotNull
    private Long discordMessageId;

    @Column(name = "total_amount")
    @NotNull
    private int totalAmount;

    @Column(name = "total_target_num")
    @NotNull
    private int totalTargetNum;

    @Column(name = "bank_name")
    @NotNull
    private String bankName;

    @Column(name = "bank_account_num")
    @NotNull
    private String bankAccountNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type")
    @NotNull
    private PayType payType;

    @Builder
    private PayRequestJpaEntity(SpaceMemberJpaEntity payCreator, Long discordMessageId, int totalAmount, int totalTargetNum, String bankName, String bankAccountNum, PayType payType) {
        this.payCreator = payCreator;
        this.discordMessageId = discordMessageId;
        this.totalAmount = totalAmount;
        this.totalTargetNum = totalTargetNum;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.payType = payType;
    }

    public static PayRequestJpaEntity create(SpaceMemberJpaEntity payCreator, Long discordMessageId, int totalAmount, int totalTargetNum, String bankName, String bankAccountNum, PayType payType) {
        return PayRequestJpaEntity.builder()
                .payCreator(payCreator)
                .discordMessageId(discordMessageId)
                .totalAmount(totalAmount)
                .totalTargetNum(totalTargetNum)
                .bankName(bankName)
                .bankAccountNum(bankAccountNum)
                .payType(payType)
                .build();
    }
}
