package space.space_spring.domain.space.domain;

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
public class SpaceJpaEntity extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private Long discordId;

    private SpaceJpaEntity(Long id, String name, Long discordId) {
        this.id = id;
        this.name = name;
        this.discordId = discordId;
    }

    public static SpaceJpaEntity create(Long id, String name, Long discordId) {
        return new SpaceJpaEntity(id, name, discordId);
    }

}
