package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.in.discord.CreateSpaceMemberInDiscordUseCase;
import space.space_spring.domain.discord.application.service.CreateSpaceMemberInDiscordService;
import space.space_spring.domain.spaceMember.application.port.in.DeleteSpaceMemberUseCase;
import space.space_spring.domain.spaceMember.application.port.in.UpdateSpaceMemberUseCase;
import space.space_spring.domain.spaceMember.application.port.out.GuildMember;

@Component
@RequiredArgsConstructor
public class SpaceMemberEventListener extends ListenerAdapter {
    private final CreateSpaceMemberInDiscordUseCase createSpaceMemberInDiscordUseCase;
    private final UpdateSpaceMemberUseCase updateSpaceMemberUseCase;
    private final DeleteSpaceMemberUseCase deleteSpaceMemberUseCase;
    @Override
    public void onGuildMemberUpdate(GuildMemberUpdateEvent event){
        //event.getMember();
        updateSpaceMemberUseCase.CheckChangeOrUpdate(mapMemberToGuildMember(event.getMember()));
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event){
        GuildMember guildMember = mapMemberToGuildMember(event.getMember());
        try {
            createSpaceMemberInDiscordUseCase.create(guildMember, event.getGuild().getIdLong());
        }catch(IllegalArgumentException e){
            System.out.println("no space but member join : cant create spaceMember");
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event){
        GuildMember guildMember = mapMemberToGuildMember(event.getMember());
        deleteSpaceMemberUseCase.delete(guildMember);

    }

    //This method return inManage=false
    private GuildMember mapMemberToGuildMember(Member member){
        return GuildMember.builder()
                .discordId(member.getIdLong())
                .isManager(false) //ToDo
                .nickname(member.getEffectiveName())
                .profileImageUrl(member.getEffectiveAvatarUrl())
                .guildDiscordId(member.getGuild().getIdLong())
                .build();

    }
}
