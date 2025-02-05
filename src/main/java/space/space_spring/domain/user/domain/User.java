package space.space_spring.domain.user.domain;

import lombok.Getter;

@Getter
public class User {

    private Long id;

    private Long discordId;

    private User(Long id, Long discordId) {
        this.id = id;
        this.discordId = discordId;
    }

    public static User create(Long id, Long discordId) {
        return new User(id, discordId);
    }
}
