package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;

@Component
@RequiredArgsConstructor
public class PayRequestTargetMapper {

    public PayRequestTargetJpaEntity toJpaEntity(SpaceMemberJpaEntity targetMemberJpaEntity, PayRequestJpaEntity payRequestJpaEntity, PayRequestTarget domain) {
        return PayRequestTargetJpaEntity.create(
                targetMemberJpaEntity,
                payRequestJpaEntity,
                domain.getRequestedAmount().getAmountInInteger()
        );
    }

    public PayRequestTarget toDomainEntity(PayRequestTargetJpaEntity jpaEntity) {
        return PayRequestTarget.of(
                jpaEntity.getId(),
                jpaEntity.getTargetMember().getId(),
                jpaEntity.getPayRequest().getId(),
                Money.of(jpaEntity.getRequestedAmount()),
                jpaEntity.isComplete()
        );
    }

}
