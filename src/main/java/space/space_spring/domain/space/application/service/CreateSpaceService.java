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
        //space가 기존에 등록이 되었는지 확인
        //이미 등록된 guild라면 예외 발생
        checkSpacePresent(loadSpacePort.loadSpaceByDiscordId(command.getGuildId()));

        //space 생성
        Space newSpace = createSpacePort.saveSpace(command.getGuildId(),
                command.getGuildName());

        //GuildMember 정보 가져오기

        //User가 없는 경우 User 생성

        //SpaceMember 생성



        return newSpace.getId();
    }

    private void checkSpacePresent(Optional<Space> space){
        space.ifPresent(value->{
            throw  new CustomException(SPACE_ALREADY_EXISTED,
                    SPACE_ALREADY_EXISTED.getMessage()+"\nspaceId: "+value.getId());
        });
    }
}
