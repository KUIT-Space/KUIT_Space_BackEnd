package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;

@Component
@RequiredArgsConstructor
public class PayRequestTargetMapper {

    private final PayRequestMapper payRequestMapper;

    public PayRequestTargetJpaEntity toJpaEntity(SpaceMemberJpaEntity targetMemberJpaEntity, PayRequestJpaEntity payRequestJpaEntity, PayRequestTarget domain) {
        return PayRequestTargetJpaEntity.createNewJpaEntity(
                targetMemberJpaEntity,
                payRequestJpaEntity,
                domain.getRequestedAmount().getAmountInInteger()
        );
    }

    public PayRequestTarget toDomainEntity(SpaceMember targetMember, PayRequestTargetJpaEntity jpaEntity) {
        return PayRequestTarget.of(
                jpaEntity.getId(),
                targetMember,
                payRequestMapper.toDomainEntity(jpaEntity.getPayRequestJpaEntity()),
                Money.of(jpaEntity.getRequestedAmount()),
                jpaEntity.isComplete()
        );
    }

}
