package space.space_spring.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.space.application.port.out.CreateSpacePort;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.spaceMember.application.port.out.CreateSpaceMemberPort;
import space.space_spring.domain.spaceMember.application.port.out.GuildMember;
import space.space_spring.domain.spaceMember.application.port.out.GuildMembers;
import space.space_spring.domain.spaceMember.application.port.out.LoadGuildMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;
import space.space_spring.domain.user.application.port.in.CreateUserUseCase;
import space.space_spring.domain.user.application.port.out.CreateUserPort;
import space.space_spring.domain.user.application.port.out.LoadUserPort;
import space.space_spring.domain.user.domain.User;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {

    private final CreateUserPort createUserPort;
    private final LoadUserPort loadUserPort;

    @Override
    public Long findOrCreateUser(GuildMember guildMember){
        //User 존재 확인
        User user=loadUserPort.loadUserByDiscordId(guildMember.getDiscordId()).orElseGet(()->{
            User newUser = User.withoutId(guildMember.getDiscordId());
            return createUserPort.createUser(newUser);
        });

        return user.getId();

    }

}
