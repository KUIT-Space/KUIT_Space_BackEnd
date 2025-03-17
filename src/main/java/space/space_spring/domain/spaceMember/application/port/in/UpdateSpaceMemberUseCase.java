package space.space_spring.domain.spaceMember.application.port.in;

import space.space_spring.domain.spaceMember.application.port.out.GuildMember;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;

public interface UpdateSpaceMemberUseCase {
    SpaceMember update(SpaceMember spaceMember);
    boolean CheckChangeOrUpdate(GuildMember guildMember);
    void updateManager(Long spaceMemberDiscordId,boolean isManager);
}
