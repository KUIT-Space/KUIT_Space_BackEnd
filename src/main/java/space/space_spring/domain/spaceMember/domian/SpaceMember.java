package space.space_spring.domain.spaceMember.domian;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.user.User;

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

    public SpaceMember setUserId(Long userId){
        this.userId =userId;
        return this;
    }

}