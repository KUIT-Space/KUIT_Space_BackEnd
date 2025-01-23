package space.space_spring.domain.spaceMember;

import lombok.Getter;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.user.User;

@Getter
public class SpaceMember {

    private Long id;

    private Space space;            // 소속된 Space -> 이거도 도메인 엔티티로 ??

    private User user;              // spaceMember의 본캐 유저 -> 이거도 도메인 엔티티로 ??

    private Long discordId;

    private SpaceMember(Long id, Space space, User user, Long discordId) {
        this.id = id;
        this.space = space;
        this.user = user;
        this.discordId = discordId;
    }

    public static SpaceMember create(Long id, Space space, User user, Long discordId) {
        return new SpaceMember(id, space, user, discordId);
    }
}
