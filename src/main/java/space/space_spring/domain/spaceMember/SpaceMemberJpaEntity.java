package space.space_spring.domain.spaceMember;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.user.User;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
public class SpaceMemberJpaEntity extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Space space;

    @ManyToOne
    private User user;

    private Long discordId;         // 디스코드 id 값


}
