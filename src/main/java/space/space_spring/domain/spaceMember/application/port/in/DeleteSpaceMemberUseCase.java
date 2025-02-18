package space.space_spring.domain.spaceMember.application.port.in;

import space.space_spring.domain.spaceMember.application.port.out.GuildMember;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

public interface DeleteSpaceMemberUseCase {
    SpaceMember delete(GuildMember guildMember);
}
