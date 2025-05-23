package space.space_spring.domain.spaceMember.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.space.adapter.out.persistence.SpaceMapper;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.domain.user.adapter.out.persistence.UserJpaEntity;
import space.space_spring.domain.user.adapter.out.persistence.UserMapper;

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
    public SpaceMemberJpaEntity toJpaEntity(SpaceMember spaceMember, SpaceJpaEntity space, UserJpaEntity user){
        return SpaceMemberJpaEntity.create(
                space,
                user,
                spaceMember.getDiscordId(),
                spaceMember.getNickname(),
                spaceMember.getProfileImageUrl(),
                spaceMember.isManager()
        );
    }

    public SpaceMemberJpaEntity updateJpaEntity(SpaceMemberJpaEntity spaceMemberJpaEntity,SpaceMember spaceMember){
        return spaceMemberJpaEntity.updateNickName(spaceMember.getNickname())
        .updateProfileImageUrl(spaceMemberJpaEntity.getProfileImageUrl())
        .updateManager(spaceMemberJpaEntity.isManager());
    }
}
