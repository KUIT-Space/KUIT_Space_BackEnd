package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.discord.CreateSpaceMemberInDiscordUseCase;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.spaceMember.application.port.in.CreateSpaceMemberUseCase;
import space.space_spring.domain.spaceMember.application.port.out.GuildMember;
import space.space_spring.domain.user.application.port.in.CreateUserUseCase;

@Service
@RequiredArgsConstructor
public class CreateSpaceMemberInDiscordService implements CreateSpaceMemberInDiscordUseCase {
    private final CreateUserUseCase createUserUseCase;
    private final CreateSpaceMemberUseCase createSpaceMemberUseCase;
    private final LoadSpacePort loadSpacePort;
    @Override
    public void create(GuildMember guildMember,Long guildId){
        Long userId = createUserUseCase.findOrCreateUser(guildMember);
        Long spaceId = loadSpacePort.loadSpaceByDiscordId(guildId).orElseThrow(()->new IllegalArgumentException()).getId();
        guildMember.createSpaceMember(spaceId,userId);

    }
}
