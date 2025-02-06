package space.space_spring.domain.pay.adapter.out.persistence;

import org.springframework.stereotype.Component;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

@Component
public class PayRequestTargetMapper {

    public PayRequestTargetJpaEntity toJpaEntity(SpaceMemberJpaEntity targetMemberJpaEntity, PayRequestJpaEntity payRequestJpaEntity, PayRequestTarget domain) {
        return PayRequestTargetJpaEntity.create(
                targetMemberJpaEntity,
                payRequestJpaEntity,
                domain.getRequestedAmount().getAmountInInteger()
        );
    }
}
