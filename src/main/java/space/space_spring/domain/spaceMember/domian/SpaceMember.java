package space.space_spring.domain.spaceMember.domian;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.user.domain.User;

@Getter
@Builder
public class SpaceMember {

    private Long id;

    private Long spaceId;

    private Long userId;

    private Long discordId;

    private String nickname;

    private String profileImageUrl;

    private boolean isManager;

    private SpaceMember(Long id, Long spaceId, Long userId, Long discordId, String nickname, String profileImageUrl, boolean isManager) {
        this.id = id;
        this.spaceId = spaceId;
        this.userId = userId;
        this.discordId = discordId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.isManager = isManager;
    }

    public static SpaceMember create(Long id, Space space, User user, Long discordId, String nickname, String profileImageUrl, boolean isManager) {
        return new SpaceMember(id, space.getId(), user.getId(), discordId, nickname, profileImageUrl, isManager);
    }

    public static SpaceMember create(Long id, Long spaceId, Long userId, Long discordId, String nickname, String profileImageUrl, boolean isManager) {
        return new SpaceMember(id, spaceId, userId, discordId, nickname, profileImageUrl, isManager);
    }
    public static SpaceMember withoutId(Long spaceId,Long userId,Long discordId,String nickname,String profileImageUrl,boolean isManager){
        return SpaceMember.builder()
                .spaceId(spaceId)
                .userId(userId)
                .discordId(discordId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .isManager(isManager)
                .build();
    }


    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof SpaceMember)) return false;
        SpaceMember spaceMember = (SpaceMember) o;

        if(!this.nickname.equals(spaceMember.getNickname())){
            return false;
        }
        if(!this.profileImageUrl.equals(spaceMember.getProfileImageUrl())){
            return false;
        }
        if(!this.id.equals(spaceMember.getId())) {
            return false;
        }
        if(!this.userId.equals(spaceMember.getUserId())){
            return false;
        }
        if(!this.spaceId.equals(spaceMember.getSpaceId())){
            return false;
        }
        if(!(this.isManager== spaceMember.isManager())){
            return false;
        }
        if(!this.discordId.equals(spaceMember.getDiscordId())){
            return false;
        }
        return true;
    }

}