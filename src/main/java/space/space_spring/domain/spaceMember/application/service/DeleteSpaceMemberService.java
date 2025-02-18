package space.space_spring.domain.spaceMember.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.spaceMember.application.port.in.DeleteSpaceMemberUseCase;
import space.space_spring.domain.spaceMember.application.port.out.DeleteSpaceMemberPort;
import space.space_spring.domain.spaceMember.application.port.out.GuildMember;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
@Service
@RequiredArgsConstructor
public class DeleteSpaceMemberService implements DeleteSpaceMemberUseCase {
    private final DeleteSpaceMemberPort deleteSpaceMemberPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    @Override
    public SpaceMember delete(GuildMember guildMember){
        SpaceMember spaceMember=loadSpaceMemberPort.loadByDiscord(guildMember.getGuildDiscordId(),guildMember.getDiscordId());
        deleteSpaceMemberPort.delete(spaceMember.getId());
        return spaceMember;
    }
}
