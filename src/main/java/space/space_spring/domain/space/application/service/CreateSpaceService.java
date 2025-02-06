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
import space.space_spring.domain.spaceMember.application.port.out.GuildMember;
import space.space_spring.domain.spaceMember.application.port.out.GuildMembers;
import space.space_spring.domain.spaceMember.application.port.out.LoadGuildMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;
import space.space_spring.domain.user.User;
import space.space_spring.domain.user.application.port.out.CreateUserPort;
import space.space_spring.domain.user.application.port.out.LoadUserPort;
import space.space_spring.global.exception.CustomException;

import java.util.List;
import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_ALREADY_EXISTED;

@Service
@RequiredArgsConstructor
@Transactional()
public class CreateSpaceService implements CreateSpaceUseCase {

    private final CreateSpacePort createSpacePort;
    private final LoadSpacePort loadSpacePort;
    private final LoadGuildMemberPort loadGuildMemberPort;
    private final CreateUserPort createUserPort;
    private final LoadUserPort loadUserPort;

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
        GuildMembers guildMembers=loadGuildMemberPort.loadAllSpaceMembers(newSpace);

        //User가 없는 경우 User 생성
        List<Long> userIdList = guildMembers.toStream().map(guildMember -> {
            return checkAndCreateUser(guildMember);
        }).toList();

        //SpaceMember 생성



        return newSpace.getId();
    }

    private void checkSpacePresent(Optional<Space> space){
        space.ifPresent(value->{
            throw  new CustomException(SPACE_ALREADY_EXISTED,
                    SPACE_ALREADY_EXISTED.getMessage()+"\nspaceId: "+value.getId());
        });
    }

    private Long checkAndCreateUser(GuildMember guildMember){
        //User 존재 확인
       User user=loadUserPort.loadUserByDiscordId(guildMember.getDiscordId()).orElseGet(()->{
           User newUser = User.withoutId(guildMember.getDiscordId());
           return createUserPort.createUser(newUser);
       });

        return user.getId();
    }
}
