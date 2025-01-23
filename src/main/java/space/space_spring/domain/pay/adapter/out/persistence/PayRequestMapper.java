package space.space_spring.domain.pay.adapter.out.persistence;

import org.springframework.stereotype.Component;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;

@Component
public class PayRequestMapper {

    PayRequestJpaEntity toJpaEntity(SpaceMemberJpaEntity payCreatorJpaEntity, PayRequest domain) {
        return PayRequestJpaEntity.create(
                payCreatorJpaEntity,
                domain.getTotalAmount().getAmountInInteger(),
                domain.getBank().getName(),
                domain.getBank().getAccountNumber(),
                domain.getPayType()
        );
    }
}
