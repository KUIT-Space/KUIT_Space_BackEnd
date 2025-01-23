package space.space_spring.domain.space;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@Getter
public class Space extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private Long discordId;

}
