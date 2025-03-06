package space.space_spring.domain.pay.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.pay.adapter.out.persistence.jpaEntity.PayRequestJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;


import java.util.List;
import java.util.Optional;

public interface SpringDataPayRequestRepository extends JpaRepository<PayRequestJpaEntity, Long> {

    Optional<List<PayRequestJpaEntity>> findListByPayCreatorAndStatus(SpaceMemberJpaEntity payCreator, BaseStatusType baseStatusType);

    Optional<PayRequestJpaEntity> findByIdAndStatus(Long id, BaseStatusType baseStatusType);
}
