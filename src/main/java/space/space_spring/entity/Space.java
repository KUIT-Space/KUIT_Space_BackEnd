package space.space_spring.entity;

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
    private String spaceProfileImg;

    public void saveSpace(String spaceName, String spaceProfileImg) {
        this.spaceName = spaceName;
        this.spaceProfileImg = spaceProfileImg;
        initializeBaseEntityFields();
    }

}
