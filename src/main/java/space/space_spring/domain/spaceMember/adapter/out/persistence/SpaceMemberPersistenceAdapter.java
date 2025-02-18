package space.space_spring.domain.spaceMember.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.space.adapter.out.persistence.SpringDataSpace;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.domain.spaceMember.application.port.out.*;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.domain.user.adapter.out.persistence.SpringDataUserRepository;
import space.space_spring.domain.user.adapter.out.persistence.UserJpaEntity;
import space.space_spring.domain.user.application.port.out.LoadUserPort;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_MEMBER_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_NOT_FOUND;

@RequiredArgsConstructor
@Repository
public class SpaceMemberPersistenceAdapter
        implements LoadSpaceMemberPort , CreateSpaceMemberPort, LoadSpaceMemberInfoPort , UpdateSpaceMemberPort ,DeleteSpaceMemberPort {

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
    @Override
    public List<SpaceMember> createSpaceMembers(List<SpaceMember> spaceMembers){
        if(spaceMembers.isEmpty()){
            return null;
        }
        SpaceJpaEntity space = spaceRepository.findById(spaceMembers.get(0).getSpaceId()).orElseThrow();
        List<SpaceMemberJpaEntity> spaceMemberJpaEntityList = new ArrayList<>();
        spaceMembers.stream().forEach(spaceMember -> {
            UserJpaEntity user = userRepository.findByDiscordId(spaceMember.getDiscordId()).orElseThrow();
            spaceMemberJpaEntityList.add(spaceMemberMapper.toJpaEntity(spaceMember,space,user));
        });

        List<SpaceMemberJpaEntity> resultSpaceMemberJpaEntityList = spaceMemberRepository.saveAll(spaceMemberJpaEntityList);

        return resultSpaceMemberJpaEntityList.stream().map(spaceMemberMapper::toDomainEntity).toList();

    }

    @Override
    public SpaceMember update(SpaceMember spaceMember){
        if(spaceMember.getId()==null){
            throw new IllegalArgumentException("spaceMember id is null");
        }

        //check spaceMember Id exist
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(spaceMember.getId()).orElseThrow(()->new IllegalArgumentException("no spaceMember ID exist"));

        SpaceMemberJpaEntity newSpaceMember = spaceMemberRepository.save(
                spaceMemberMapper.updateJpaEntity(spaceMemberJpaEntity,spaceMember));

        return spaceMemberMapper.toDomainEntity(newSpaceMember);
    }

    @Override
    public SpaceMember loadByDiscord(Long spaceDiscordId , Long spaceMemberDiscordId){
        SpaceJpaEntity spaceJpaEntity = spaceRepository.findByDiscordId(spaceDiscordId).orElseThrow(()->new CustomException(SPACE_NOT_FOUND));
        return spaceMemberMapper.toDomainEntity(spaceMemberRepository.findBySpaceIdAndDiscordId(spaceJpaEntity.getId(),spaceMemberDiscordId).orElseThrow(()->new CustomException(SPACE_MEMBER_NOT_FOUND)));
    }

    @Override
    public boolean delete(Long spaceId){
        //ToDo change to soft delete
        spaceMemberRepository.delete(spaceMemberRepository.findById(spaceId).orElseThrow(()->new CustomException(SPACE_MEMBER_NOT_FOUND)));

        return true;
    }
}
