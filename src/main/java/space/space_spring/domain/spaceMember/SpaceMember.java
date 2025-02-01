package space.space_spring.domain.spaceMember;

import lombok.Getter;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.user.User;

@Getter
public class SpaceMember {

    private Long id;

    private Space space;

    private User user;

    private Long discordId;

    private String nickname;

    private String profileImageUrl;

    private boolean isManager;

    private SpaceMember(Long id, Space space, User user, Long discordId, String nickname, String profileImageUrl, boolean isManager) {
        this.id = id;
        this.space = space;
        this.user = user;
        this.discordId = discordId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.isManager = isManager;
    }

    public static SpaceMember create(Long id, Space space, User user, Long discordId, String nickname, String profileImageUrl, boolean isManager) {
        return new SpaceMember(id, space, user, discordId, nickname, profileImageUrl, isManager);
    }
}