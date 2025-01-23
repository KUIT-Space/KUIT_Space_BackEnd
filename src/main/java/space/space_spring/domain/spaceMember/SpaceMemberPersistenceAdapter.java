package space.space_spring.domain.spaceMember;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.space.SpaceMapper;
import space.space_spring.domain.user.User;
import space.space_spring.domain.user.UserMapper;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_CREATOR_IS_NOT_IN_SPACE;

@RequiredArgsConstructor
@Repository
public class SpaceMemberPersistenceAdapter implements LoadSpaceMemberPort {

    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpaceMemberMapper spaceMemberMapper;
    private final SpaceMapper spaceMapper;
    private final UserMapper userMapper;

    @Override
    public SpaceMember loadSpaceMember(Long id) {
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(id).orElseThrow(() ->
                new CustomException(PAY_CREATOR_IS_NOT_IN_SPACE));

        Space space = spaceMapper.mapToDomainEntity(spaceMemberJpaEntity.getSpace());
        User user = userMapper.mapToDomainEntity(spaceMemberJpaEntity.getUser());
        return spaceMemberMapper.mapToDomainEntity(space, user, spaceMemberJpaEntity);
    }
}
