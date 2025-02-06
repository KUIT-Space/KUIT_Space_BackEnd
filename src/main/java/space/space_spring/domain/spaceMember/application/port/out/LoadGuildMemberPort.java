package space.space_spring.domain.spaceMember.application.port.out;

import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;

public interface LoadGuildMemberPort {

    //Overloading set
//    SpaceMember loadSpaceMember(Long spaceDiscordId,Long spaceMemberDiscordId);
//    SpaceMember loadSpaceMember(Space space, SpaceMember spaceMember);
//    SpaceMember loadSpaceMember(Long spaceDiscordId, SpaceMember spaceMember);
    SpaceMember loadSpaceMember(Space space, Long spaceMemberDiscordId);
//    SpaceMember loadSpaceMember(Space space, Long spaceMemberDiscordId,boolean needSpaceMemberId);
//    SpaceMember loadSpaceMember(Long spaceDiscordId,Long spaceMemberDiscordId,boolean needSpaceMemberId);



//    SpaceMembers loadAllSpaceMembers(Long spaceDiscordId);
    SpaceMembers loadAllSpaceMembers(Space space);
}
