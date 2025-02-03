package space.space_spring.domain.space.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.pay.adapter.out.persistence.PayRequestTargetJpaEntity;
import space.space_spring.domain.space.domain.SpaceJpaEntity;

public interface SpringDataSpace extends JpaRepository<SpaceJpaEntity, Long> {
    SpaceJpaEntity findByDiscordId(Long discordId);
}
