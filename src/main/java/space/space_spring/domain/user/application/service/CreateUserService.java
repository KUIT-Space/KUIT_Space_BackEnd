package space.space_spring.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.spaceMember.application.port.out.GuildMember;
import space.space_spring.domain.user.application.port.in.CreateUserUseCase;
import space.space_spring.domain.user.application.port.out.CreateUserPort;
import space.space_spring.domain.user.application.port.out.LoadUserPort;
import space.space_spring.domain.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional
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
