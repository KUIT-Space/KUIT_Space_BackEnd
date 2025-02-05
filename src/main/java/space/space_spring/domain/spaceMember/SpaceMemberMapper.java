package space.space_spring.domain.spaceMember;

import org.springframework.stereotype.Component;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.user.domain.User;

@Component
public class SpaceMemberMapper {

    public SpaceMember mapToDomainEntity(Space space, User user, SpaceMemberJpaEntity spaceMemberJpaEntity) {
        return SpaceMember.create(
                spaceMemberJpaEntity.getId(),
                space,
                user,
                spaceMemberJpaEntity.getDiscordId(),
                spaceMemberJpaEntity.getNickname(),
                spaceMemberJpaEntity.getProfileImageUrl(),
                spaceMemberJpaEntity.isManager()
        );
    }
}
