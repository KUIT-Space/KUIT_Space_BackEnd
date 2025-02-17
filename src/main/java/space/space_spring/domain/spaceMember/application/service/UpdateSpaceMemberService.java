package space.space_spring.domain.spaceMember.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.spaceMember.application.port.in.UpdateSpaceMemberUseCase;
import space.space_spring.domain.spaceMember.application.port.out.GuildMember;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.application.port.out.UpdateSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
@Service
@RequiredArgsConstructor
public class UpdateSpaceMemberService implements UpdateSpaceMemberUseCase {
    private final UpdateSpaceMemberPort updateSpaceMemberPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;

    @Override
    public SpaceMember update(SpaceMember spaceMember){
        return updateSpaceMemberPort.update(spaceMember);
    }

    @Override
    public boolean CheckChangeOrUpdate(GuildMember guildMember){
        SpaceMember spaceMember = loadSpaceMemberPort.loadByDiscord(guildMember.getGuildDiscordId(),guildMember.getDiscordId());
        if(!guildMember.checkChangeSpaceMember(spaceMember)){
            return false;
        }
        SpaceMember newSpaceMember = guildMember.createSpaceMember(spaceMember.getSpaceId(), spaceMember.getUserId());
        SpaceMember newSpaceMemberWithId = getUpdateSpaceMember(spaceMember.getId(), newSpaceMember);
        updateSpaceMemberPort.update(newSpaceMemberWithId);
        return true;
    }

    private SpaceMember getUpdateSpaceMember(Long spaceMemberId,SpaceMember newSpaceMember){
        return SpaceMember.create
                (spaceMemberId
                        ,newSpaceMember.getSpaceId(),newSpaceMember.getUserId(), newSpaceMember.getDiscordId(), newSpaceMember.getNickname(), newSpaceMember.getProfileImageUrl(), newSpaceMember.isManager());
    }
}
