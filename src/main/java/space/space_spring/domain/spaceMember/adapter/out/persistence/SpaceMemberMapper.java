package space.space_spring.domain.spaceMember.adapter.out.persistence;

import org.springframework.stereotype.Component;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.domain.user.User;

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
