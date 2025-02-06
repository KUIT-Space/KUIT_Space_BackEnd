package space.space_spring.domain.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.domain.user.UserJpaEntity;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, Long> {
}
