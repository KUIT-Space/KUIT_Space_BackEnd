package space.space_spring.domain.pay.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.pay.adapter.out.persistence.jpaEntity.PayRequestJpaEntity;
import space.space_spring.domain.pay.adapter.out.persistence.jpaEntity.PayRequestTargetJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;
import java.util.Optional;

public interface SpringDataPayRequestTargetRepository extends JpaRepository<PayRequestTargetJpaEntity, Long> {

    Optional<List<PayRequestTargetJpaEntity>> findListByTargetMemberAndStatus(SpaceMemberJpaEntity targetMemberJpaEntity, BaseStatusType baseStatusType);

    Optional<List<PayRequestTargetJpaEntity>> findByPayRequestAndStatus(PayRequestJpaEntity payRequestJpaEntity, BaseStatusType baseStatusType);

    Optional<PayRequestTargetJpaEntity> findByIdAndStatus(Long id, BaseStatusType baseStatusType);

    Optional<List<PayRequestTargetJpaEntity>> findAllByIdInAndStatus(List<Long> ids, BaseStatusType baseStatusType);
}
