package space.space_spring.domain.pay.adapter.out.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.pay.adapter.out.persistence.jpaEntity.PayRequestJpaEntity;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

import space.space_spring.global.common.entity.BaseInfo;
import space.space_spring.global.util.NaturalNumber;


@Component
@RequiredArgsConstructor
public class PayRequestMapper {

    public PayRequestJpaEntity toJpaEntity(SpaceMemberJpaEntity payCreatorJpaEntity, PayRequest domain) {
        return PayRequestJpaEntity.create(
                payCreatorJpaEntity,
                domain.getDiscordMessageId(),
                domain.getTotalAmount().getAmountInInteger(),
                domain.getTotalTargetNum().getNumber(),
                domain.getBank().getName(),
                domain.getBank().getAccountNumber(),
                domain.getPayType(),
                domain.getBaseInfo().getCreatedAt(),
                domain.getBaseInfo().getLastModifiedAt(),
                domain.getBaseInfo().getStatus()
        );
    }

    public PayRequest toDomainEntity(PayRequestJpaEntity jpaEntity) {
        Bank bank = Bank.of(jpaEntity.getBankName(), jpaEntity.getBankAccountNum());
        BaseInfo baseInfo = BaseInfo.of(jpaEntity.getCreatedAt(), jpaEntity.getLastModifiedAt(), jpaEntity.getStatus());

        return PayRequest.create(
                jpaEntity.getId(),
                jpaEntity.getPayCreator().getId(),
                jpaEntity.getDiscordMessageId(),
                Money.of(jpaEntity.getTotalAmount()),
                NaturalNumber.of(jpaEntity.getTotalTargetNum()),
                bank,
                jpaEntity.getPayType(),
                baseInfo
        );
    }
}
