package space.space_spring.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "Spaces")
public class Space extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "space_id")
    private Long spaceId;

    @Column(name = "space_name")
    private String spaceName;

    @Column(name = "space_profile_img")
    @Nullable
    private String spaceProfileImg;
}
