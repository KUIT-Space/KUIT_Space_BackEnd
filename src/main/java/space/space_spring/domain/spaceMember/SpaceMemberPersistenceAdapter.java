package space.space_spring.domain.spaceMember;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.global.exception.CustomException;

import java.util.List;
import java.util.function.Consumer;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Repository
public class SpaceMemberPersistenceAdapter implements LoadSpaceMemberPort, LoadSpaceMemberInfoPort {

    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpaceMemberMapper spaceMemberMapper;

    @Override
    public SpaceMember loadById(Long id) {
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(id).orElseThrow(() ->
                new CustomException(SPACE_MEMBER_NOT_FOUND));

        return spaceMemberMapper.toDomainEntity(spaceMemberJpaEntity);
    }

    @Override
    public List<SpaceMember> loadAllById(List<Long> ids) {
        List<SpaceMemberJpaEntity> allById = spaceMemberRepository.findAllById(ids);

        return allById.stream().map(spaceMemberMapper::toDomainEntity)
                .toList();
    }

    @Override
    public NicknameAndProfileImage loadNicknameAndProfileImageById(Long spaceMemberId) {
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(spaceMemberId).orElseThrow(() ->
                new CustomException(SPACE_MEMBER_NOT_FOUND));

        return NicknameAndProfileImage.of(spaceMemberJpaEntity.getNickname(), spaceMemberJpaEntity.getProfileImageUrl());
    }
}
