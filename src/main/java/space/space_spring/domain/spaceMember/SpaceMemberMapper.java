package space.space_spring.domain.spaceMember;

import org.springframework.stereotype.Component;

@Component
public class SpaceMemberMapper {

    public SpaceMember mapToDomainEntity(SpaceMemberJpaEntity spaceMemberJpaEntity) {
        return SpaceMember.create(
                spaceMemberJpaEntity.getId(),
                spaceMemberJpaEntity.getSpace(),
                spaceMemberJpaEntity.getUser(),
                spaceMemberJpaEntity.getDiscordId()
        );
    }
}
