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
import space.space_spring.global.exception.CustomException;

import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_ALREADY_EXISTED;

@Service
@RequiredArgsConstructor
@Transactional()
public class CreateSpaceService implements CreateSpaceUseCase {

    private final CreateSpacePort createSpacePort;
    private final LoadSpacePort loadSpacePort;
    @Override
    @Transactional
    public Long createSpace(CreateSpaceCommand command){

        checkSpacePresent(loadSpacePort.loadSpaceByDiscordId(command.getGuildId()));
        return createSpacePort.saveSpace(command.getGuildId(),
                command.getGuildName());
    }

    private void checkSpacePresent(Optional<Space> space){
        space.ifPresent(value->{
            throw  new CustomException(SPACE_ALREADY_EXISTED,
                    SPACE_ALREADY_EXISTED.getMessage()+"\nspaceId: "+value.getId());
        });
    }
}
