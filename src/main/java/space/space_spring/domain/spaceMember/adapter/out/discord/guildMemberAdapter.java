package space.space_spring.domain.spaceMember.adapter.out.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.spaceMember.application.port.out.GuildMember;
import space.space_spring.domain.spaceMember.application.port.out.GuildMembers;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.application.port.out.LoadGuildMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class guildMemberAdapter implements LoadGuildMemberPort {
    final private JDA jda;
    final LoadSpaceMemberPort loadSpaceMemberPort;

    public GuildMember loadSpaceMember(Space space, Long spaceMemberDiscordId, Long spaceMemberId){
        Guild guild=jda.getGuildById(space.getId());
        Member member =guild.getMemberById(spaceMemberDiscordId);
        GuildMember spaceMember = guildToSpaceMember(member);

        return spaceMember;
    }

//    @Override
//    public SpaceMember loadSpaceMember(Long spaceDiscordId, Long spaceMemberDiscordId){
//        return loadSpaceMember(spaceDiscordId,spaceMemberDiscordId,null);
//    }
//
//    @Override
//    public SpaceMember loadSpaceMember(Space space, SpaceMember spaceMember){
//        return loadSpaceMember(space.getDiscordId(),spaceMember.getDiscordId(), spaceMember.getId());
//    }
//    @Override
//    public SpaceMember loadSpaceMember(Long spaceDiscordId, SpaceMember spaceMember){
//        return loadSpaceMember(spaceDiscordId,spaceMember.getDiscordId(),spaceMember.getId());
//    }
    @Override
    public GuildMember loadSpaceMember(Space space, Long spaceMemberDiscordId){
        return loadSpaceMember(space,spaceMemberDiscordId,null);
    }
//    @Override
//    public SpaceMember loadSpaceMember(Space space, Long spaceMemberDiscordId,boolean needSpaceMemberId){
//        if(needSpaceMemberId){
//            SpaceMember
//
//        }
//        return loadSpaceMember(space.getDiscordId(),spaceMemberDiscordId,null);
//    }
//    @Override
//    public SpaceMember loadSpaceMember(Long spaceDiscordId,Long spaceMemberDiscordId,boolean needSpaceMemberId);


    @Override
    public GuildMembers loadAllSpaceMembers(Space space){
        Guild guild=jda.getGuildById(space.getDiscordId());
        //ToDo. change completableFuture
        List<GuildMember> guildMemberList = guild.getMembers().stream().map(member ->
            guildToSpaceMember(member)
        ).collect(Collectors.toList());
        System.out.println("\nGuild Member load api success\n");
        for( GuildMember guildMember: guildMemberList){
            System.out.println("Member:"+guildMember.getNickname()+"\n");
        }

        return GuildMembers.of(space,guildMemberList);
    }

//    private Long getSpaceMemberIdByDiscordId(Long discordId){
//
//    }

    //This method return user=null, inManage=false, id=null
    private GuildMember guildToSpaceMember(Member member){
        return GuildMember.builder()
                .discordId(member.getIdLong())
                .isManager(false) //ToDo
                .nickname(member.getEffectiveName())
                .profileImageUrl(member.getEffectiveAvatarUrl())
                .build();

    }

}
