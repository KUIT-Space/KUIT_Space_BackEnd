package space.space_spring.domain.spaceMember;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import space.space_spring.domain.space.SpaceJpaEntity;
import space.space_spring.domain.user.UserJpaEntity;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@Getter
public class SpaceMemberJpaEntity extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private SpaceJpaEntity space;

    @ManyToOne
    private UserJpaEntity user;

    private Long discordId;         // 디스코드 id 값


}
