package space.space_spring.domain.space.adapter.out.persistence;

import org.springframework.stereotype.Component;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.space.domain.SpaceJpaEntity;

@Component
public class SpaceMapper {

    public Space mapToDomainEntity(SpaceJpaEntity jpaEntity) {
        return Space.create(jpaEntity.getId(), jpaEntity.getName(), jpaEntity.getDiscordId());
    }

    public SpaceJpaEntity mapToJpaEntity(Space domain) {
        return SpaceJpaEntity.create(domain.getId(), domain.getName(), domain.getDiscordId());
    }

}
