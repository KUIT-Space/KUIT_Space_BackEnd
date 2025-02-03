package space.space_spring.domain.space.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;
import space.space_spring.domain.space.SpaceMapper;
import space.space_spring.domain.space.application.port.out.CreateSpacePort;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.space.domain.SpaceJpaEntity;

@RequiredArgsConstructor
@Repository
public class SpacePersistenceAdapter implements CreateSpacePort , LoadSpacePort {
    private final SpringDataSpace spaceRepository;
    private final SpaceMapper spaceMapper;
    @Override
    public Long saveSpace(Long guildId,String guildName){

        Space space = Space.withoutId(guildId,guildName);
        SpaceJpaEntity spaceJpaEntity = spaceMapper.mapToJpaEntity(space);
        SpaceJpaEntity resultSpaceJpaEntity = spaceRepository.save(spaceJpaEntity);
        return resultSpaceJpaEntity.getId();
    }

    @Override
    public Space loadSpaceByDiscordId(Long discordId){
        SpaceJpaEntity spaceJpaEntity = spaceRepository.findByDiscordId(discordId);
        if(spaceJpaEntity==null){
            return null;
        }
        return spaceMapper.mapToDomainEntity(spaceJpaEntity);
    }

}
