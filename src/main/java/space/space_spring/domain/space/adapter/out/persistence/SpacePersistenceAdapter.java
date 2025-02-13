package space.space_spring.domain.space.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.space.application.port.out.CreateSpacePort;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.space.domain.SpaceJpaEntity;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class SpacePersistenceAdapter implements CreateSpacePort , LoadSpacePort {
    private final SpringDataSpace spaceRepository;
    private final SpaceMapper spaceMapper;
    @Override
    public Space saveSpace(Long guildId,String guildName){

        Space space = Space.withoutId(guildId,guildName);
        SpaceJpaEntity spaceJpaEntity = spaceMapper.toJpaEntity(space);
        Space resultSpace = spaceMapper.toDomainEntity(spaceRepository.save(spaceJpaEntity));
        return resultSpace;
    }

    @Override
    public Optional<Space> loadSpaceByDiscordId(Long discordId){
        //없으면 Optional.empty() 반환
        return spaceRepository.findByDiscordId(discordId)
                .map(spaceMapper::toDomainEntity);


    }
    @Override
    public Optional<Space> loadSpaceById(Long id){
        return spaceRepository.findById(id)
                .map(spaceMapper::toDomainEntity);
    }

}
