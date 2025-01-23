package space.space_spring.domain.space;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class SpaceMapper {

    public Space mapToDomainEntity(SpaceJpaEntity jpaEntity) {
        return Space.create(jpaEntity.getId(), jpaEntity.getName(), jpaEntity.getDiscordId());
    }

    public SpaceJpaEntity mapToJpaEntity(Space domain) {
        return SpaceJpaEntity.create(domain.getId(), domain.getName(), domain.getDiscordId());
    }

}
