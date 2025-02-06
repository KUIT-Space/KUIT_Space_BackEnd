package space.space_spring.domain.pay.adapter.out.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Pay_Request_Target")
@Getter
public class PayRequestTargetJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "pay_request_target_id")
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_member_id")
    @NotNull
    private SpaceMemberJpaEntity targetMember;           // 정산 대상 멤버

    @ManyToOne
    @JoinColumn(name = "pay_request_id")
    @NotNull
    private PayRequestJpaEntity payRequestJpaEntity;

    @Column(name = "requested_amount")
    @NotNull
    private int requestedAmount;

    @Column(name = "is_complete")
    @NotNull
    private boolean isComplete;

    @Builder
    private PayRequestTargetJpaEntity(SpaceMemberJpaEntity targetMember, PayRequestJpaEntity payRequestJpaEntity, int requestedAmount, boolean isComplete) {
        this.targetMember = targetMember;
        this.payRequestJpaEntity = payRequestJpaEntity;
        this.requestedAmount = requestedAmount;
        this.isComplete = isComplete;
    }

    public static PayRequestTargetJpaEntity createNewJpaEntity(SpaceMemberJpaEntity targetMember, PayRequestJpaEntity payRequestJpaEntity, int requestedAmount) {
        return PayRequestTargetJpaEntity.builder()
                .targetMember(targetMember)
                .payRequestJpaEntity(payRequestJpaEntity)
                .requestedAmount(requestedAmount)
                .isComplete(false)
                .build();
    }

    public static PayRequestTargetJpaEntity of(SpaceMemberJpaEntity targetMember, PayRequestJpaEntity payRequestJpaEntity, int requestedAmount, boolean isComplete) {
        return PayRequestTargetJpaEntity.builder()
                .targetMember(targetMember)
                .payRequestJpaEntity(payRequestJpaEntity)
                .requestedAmount(requestedAmount)
                .isComplete(isComplete)
                .build();
    }
}
