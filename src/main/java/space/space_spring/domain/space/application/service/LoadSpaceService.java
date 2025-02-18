package space.space_spring.domain.space.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.space.application.port.in.LoadSpaceUseCase;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.space.domain.Space;
@Service
@RequiredArgsConstructor
public class LoadSpaceService implements LoadSpaceUseCase {
    private final LoadSpacePort loadSpacePort;
    @Override
    public Space load(Long spaceId){
        return loadSpacePort.loadSpaceById(spaceId).orElseGet(null);
    }

    @Override
    public Space loadByDiscordId(Long discordId){
        return loadSpacePort.loadSpaceByDiscordId(discordId).orElseGet(null);
    }
}
