package space.space_spring.domain.spaceMember.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

import java.util.List;

public interface SpringDataSpaceMemberRepository extends JpaRepository<SpaceMemberJpaEntity, Long> {
    List<SpaceMemberJpaEntity> findBySpaceId(Long spaceId);

}
