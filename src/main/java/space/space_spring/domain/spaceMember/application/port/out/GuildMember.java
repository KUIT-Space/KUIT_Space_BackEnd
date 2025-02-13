package space.space_spring.domain.spaceMember.application.port.out;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

@Builder
@Getter
public class GuildMember {


    private Long discordId;

    private String nickname;

    private String profileImageUrl;

    private boolean isManager;

    private GuildMember(Long discordId, String nickname, String profileImageUrl, boolean isManager){
        this.discordId=discordId;
        this.nickname=nickname;
        this.profileImageUrl=profileImageUrl;
        this.isManager=isManager;

    }

    public SpaceMember createSpaceMember(Long spaceId, Long userId){
        return SpaceMember.withoutId(spaceId,userId,this.discordId,this.nickname,this.profileImageUrl,this.isManager);
    }

}
