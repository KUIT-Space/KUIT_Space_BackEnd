package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequest;

import space.space_spring.domain.spaceMember.adapter.out.persistence.SpaceMemberMapper;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

import space.space_spring.global.util.NaturalNumber;


@Component
@RequiredArgsConstructor
public class PayRequestMapper {

    private final SpaceMemberMapper spaceMemberMapper;

    PayRequestJpaEntity toJpaEntity(SpaceMemberJpaEntity payCreatorJpaEntity, PayRequest domain) {
        return PayRequestJpaEntity.createNewJpaEntity(
                payCreatorJpaEntity,
                domain.getTotalAmount().getAmountInInteger(),
                domain.getTotalTargetNum().getNumber(),
                domain.getBank().getName(),
                domain.getBank().getAccountNumber(),
                domain.getPayType()
        );
    }

    public PayRequest toDomainEntity(SpaceMember payCreator, PayRequestJpaEntity jpaEntity) {
        Bank bank = Bank.of(jpaEntity.getBankName(), jpaEntity.getBankAccountNum());

        return PayRequest.of(
                jpaEntity.getId(),
                payCreator,
                Money.of(jpaEntity.getTotalAmount()),
                Money.of(jpaEntity.getReceivedAmount()),
                NaturalNumber.of(jpaEntity.getTotalTargetNum()),
                NaturalNumber.of(jpaEntity.getSendCompleteTargetNum()),
                bank,
                jpaEntity.isComplete(),
                jpaEntity.getPayType()
        );
    }

    public PayRequest toDomainEntity(PayRequestJpaEntity jpaEntity) {
        return toDomainEntity(spaceMemberMapper.toDomainEntity(jpaEntity.getPayCreator()), jpaEntity);
    }
}
