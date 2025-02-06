package space.space_spring.domain.spaceMember.application.port.out;

import space.space_spring.domain.space.domain.Space;

import java.util.List;
import java.util.stream.Stream;

public class GuildMembers {
    final List<GuildMember> guildMembers;
    final Space space;

    private GuildMembers(Space space, List<GuildMember> guildMemberList){
        this.space=space;
        this.guildMembers=guildMemberList;
    }

    public static GuildMembers of(Space space, List<GuildMember> guildMemberList){
        return new GuildMembers(space,guildMemberList);
    }

    public Stream<GuildMember> toStream(){
        return guildMembers.stream();
    }
}
