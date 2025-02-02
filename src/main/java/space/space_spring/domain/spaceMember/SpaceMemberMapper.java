package space.space_spring.domain.spaceMember;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.space.SpaceMapper;
import space.space_spring.domain.user.UserMapper;

@Component
@RequiredArgsConstructor
public class SpaceMemberMapper {

    private final UserMapper userMapper;
    private final SpaceMapper spaceMapper;

    public SpaceMember toDomainEntity(SpaceMemberJpaEntity jpaEntity) {
        return SpaceMember.create(
                jpaEntity.getId(),
                spaceMapper.toDomainEntity(jpaEntity.getSpace()),
                userMapper.toDomainEntity(jpaEntity.getUser()),
                jpaEntity.getDiscordId(),
                jpaEntity.getNickname(),
                jpaEntity.getProfileImageUrl(),
                jpaEntity.isManager()
        );
    }
}
