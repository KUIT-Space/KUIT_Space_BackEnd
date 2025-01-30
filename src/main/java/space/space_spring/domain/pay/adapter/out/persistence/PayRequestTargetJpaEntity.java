package space.space_spring.domain.pay.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Pay_Request_Target")
public class PayRequestTargetJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "pay_request_target_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_member_id")
    private SpaceMemberJpaEntity targetMember;           // 정산 대상 멤버

    @ManyToOne
    @JoinColumn(name = "pay_request_id")
    private PayRequestJpaEntity payRequestJpaEntity;

    @Column(name = "requsted_amount")
    private int requestedAmount;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Builder
    private PayRequestTargetJpaEntity(SpaceMemberJpaEntity targetMember, PayRequestJpaEntity payRequestJpaEntity, int requestedAmount) {
        this.targetMember = targetMember;
        this.payRequestJpaEntity = payRequestJpaEntity;
        this.requestedAmount = requestedAmount;
        this.isComplete = false;
    }

    public static PayRequestTargetJpaEntity create(SpaceMemberJpaEntity targetMember, PayRequestJpaEntity payRequestJpaEntity, int requestedAmount) {
        return PayRequestTargetJpaEntity.builder()
                .targetMember(targetMember)
                .payRequestJpaEntity(payRequestJpaEntity)
                .requestedAmount(requestedAmount).build();
    }


}
