package space.space_spring.domain.space.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.space.domain.SpaceJpaEntity;

import java.util.Optional;

public interface SpringDataSpace extends JpaRepository<SpaceJpaEntity, Long> {
    Optional<SpaceJpaEntity> findByDiscordId(Long discordId);
}
