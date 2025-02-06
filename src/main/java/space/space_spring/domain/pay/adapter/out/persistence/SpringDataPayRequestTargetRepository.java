package space.space_spring.domain.pay.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;

import java.util.List;
import java.util.Optional;

public interface SpringDataPayRequestTargetRepository extends JpaRepository<PayRequestTargetJpaEntity, Long> {

    Optional<List<PayRequestTargetJpaEntity>> findListByTargetMember(SpaceMemberJpaEntity targetMemberJpaEntity);
}
