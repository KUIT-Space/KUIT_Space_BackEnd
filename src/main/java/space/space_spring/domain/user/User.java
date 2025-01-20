package space.space_spring.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
public class User extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private Long discordId;
}
