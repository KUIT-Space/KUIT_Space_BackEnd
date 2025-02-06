package space.space_spring.domain.pay.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayRequestTargetJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private SpaceMemberJpaEntity targetMember;           // 정산 대상 멤버

    @ManyToOne
    private PayRequestJpaEntity payRequestJpaEntity;

    private int requestedAmount;

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
