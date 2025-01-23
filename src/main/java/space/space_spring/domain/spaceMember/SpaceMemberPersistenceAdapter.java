package space.space_spring.domain.spaceMember;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.LoadSpaceMemberPort;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_CREATOR_IS_NOT_IN_SPACE;

@RequiredArgsConstructor
@Repository
public class SpaceMemberPersistenceAdapter implements LoadSpaceMemberPort {

    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpaceMemberMapper spaceMemberMapper;

    @Override
    public SpaceMember loadSpaceMember(Long id) {
        // 이거도 QueryDSL 적용하는 걸로 바꿔야 할 듯
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(id).orElseThrow(() ->
                new CustomException(PAY_CREATOR_IS_NOT_IN_SPACE));

        return spaceMemberMapper.mapToDomainEntity(
        )
    }
}
