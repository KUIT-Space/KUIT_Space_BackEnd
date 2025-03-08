package space.space_spring.domain.pay.adapter.out.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.pay.adapter.out.persistence.jpaEntity.PayRequestJpaEntity;
import space.space_spring.domain.pay.adapter.out.persistence.jpaEntity.PayRequestTargetJpaEntity;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseInfo;

@Component
@RequiredArgsConstructor
public class PayRequestTargetMapper {

    public PayRequestTargetJpaEntity toJpaEntity(SpaceMemberJpaEntity targetMemberJpaEntity, PayRequestJpaEntity payRequestJpaEntity, PayRequestTarget domain) {
        return PayRequestTargetJpaEntity.create(
                targetMemberJpaEntity,
                payRequestJpaEntity,
                domain.getRequestedAmount().getAmountInInteger(),
                domain.getBaseInfo().getCreatedAt(),
                domain.getBaseInfo().getLastModifiedAt(),
                domain.getBaseInfo().getStatus()
        );
    }

    public PayRequestTarget toDomainEntity(PayRequestTargetJpaEntity jpaEntity) {
        BaseInfo baseInfo = BaseInfo.of(jpaEntity.getCreatedAt(), jpaEntity.getLastModifiedAt(), jpaEntity.getStatus());

        return PayRequestTarget.of(
                jpaEntity.getId(),
                jpaEntity.getTargetMember().getId(),
                jpaEntity.getPayRequest().getId(),
                Money.of(jpaEntity.getRequestedAmount()),
                jpaEntity.isComplete(),
                baseInfo
        );
    }
}
