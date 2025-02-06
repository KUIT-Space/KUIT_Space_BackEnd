package space.space_spring.domain.spaceMember;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.space.SpaceMapper;
import space.space_spring.domain.user.User;
import space.space_spring.domain.user.UserMapper;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Repository
public class SpaceMemberPersistenceAdapter implements LoadSpaceMemberPort {

    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpaceMemberMapper spaceMemberMapper;

    @Override
    public SpaceMember loadSpaceMemberById(Long id) {
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(id).orElseThrow(() ->
                new CustomException(SPACE_MEMBER_NOT_FOUND));

        return spaceMemberMapper.toDomainEntity(spaceMemberJpaEntity);
    }
}
