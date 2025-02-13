package space.space_spring.domain.spaceMember.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.space.adapter.out.persistence.SpringDataSpace;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberInfoPort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.application.port.out.CreateSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.domain.user.UserJpaEntity;
import space.space_spring.domain.user.adapter.out.persistence.SpringDataUserRepository;
import space.space_spring.domain.user.application.port.out.LoadUserPort;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Repository
public class SpaceMemberPersistenceAdapter implements LoadSpaceMemberPort , CreateSpaceMemberPort, LoadSpaceMemberInfoPort {

    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpaceMemberMapper spaceMemberMapper;
    private final LoadSpacePort loadSpacePort;
    private final LoadUserPort loadUserPort;
    private final SpringDataUserRepository userRepository;
    private final SpringDataSpace spaceRepository;

    @Override
    public SpaceMember loadSpaceMemberById(Long id) {
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(id).orElseThrow(() ->
                new CustomException(SPACE_MEMBER_NOT_FOUND));

        return spaceMemberMapper.toDomainEntity(spaceMemberJpaEntity);
    }

    @Override
    public SpaceMember createSpaceMember(SpaceMember spaceMember){
//        Space space = loadSpacePort.loadSpaceById(spaceMember.getSpaceId()).orElseThrow();
//        User user = loadUserPort.loadUser(spaceMember.getId()).orElseThrow();
        SpaceJpaEntity space = spaceRepository.findById(spaceMember.getSpaceId()).orElseThrow();
        UserJpaEntity user = userRepository.findByDiscordId(spaceMember.getDiscordId()).orElseThrow();

        SpaceMemberJpaEntity spaceMemberJpaEntity=spaceMemberMapper.toJpaEntity(spaceMember,space,user);
        SpaceMemberJpaEntity resultSpaceMemberJpaEntity = spaceMemberRepository.save(spaceMemberJpaEntity);

        return spaceMemberMapper.toDomainEntity(resultSpaceMemberJpaEntity);
    }

    @Override
    public List<SpaceMember> loadSpaceMemberBySpaceId(Long spaceId){
        return spaceMemberRepository.findBySpaceId(spaceId).stream().map(spaceMemberMapper::toDomainEntity).toList();
    }



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
