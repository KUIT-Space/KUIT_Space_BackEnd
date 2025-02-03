package space.space_spring.domain.space.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.space.adapter.out.persistence.SpacePersistenceAdapter;
import space.space_spring.domain.space.application.port.in.CreateSpaceCommand;
import space.space_spring.domain.space.application.port.in.CreateSpaceUseCase;
import space.space_spring.domain.space.application.port.out.CreateSpacePort;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.space.domain.Space;

@Service
@RequiredArgsConstructor
@Transactional()
public class CreateSpaceService implements CreateSpaceUseCase {

    private final CreateSpacePort createSpacePort;
    private final LoadSpacePort loadSpacePort;
    @Override
    @Transactional
    public Long createSpace(CreateSpaceCommand command){
        Space space=loadSpacePort.loadSpaceByDiscordId(command.getGuildId());
        if(space!=null){
            return null;
        }
        return createSpacePort.saveSpace(command.getGuildId(),
                command.getGuildName());
    }
}
