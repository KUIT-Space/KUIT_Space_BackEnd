package space.space_spring.domain.user.application.port.in;

import space.space_spring.domain.spaceMember.application.port.out.GuildMember;
import space.space_spring.domain.spaceMember.application.port.out.GuildMembers;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;

public interface CreateUserUseCase {
    //SpaceMembers findOrCreateUsers(GuildMembers guildMembers);

    Long findOrCreateUser(GuildMember guildMember);

}
