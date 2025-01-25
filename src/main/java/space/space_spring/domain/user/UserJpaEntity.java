package space.space_spring.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJpaEntity extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private Long discordId;

    private UserJpaEntity(Long id, Long discordId) {
        this.id = id;
        this.discordId = discordId;
    }

    public static UserJpaEntity create(Long id, Long discordId) {
        return new UserJpaEntity(id, discordId);
    }
}
