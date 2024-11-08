package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Spaces")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private Space(String spaceName, String spaceProfileImg) {
        this.spaceName = spaceName;
        this.spaceProfileImg = spaceProfileImg;
    }

    public static Space create(String spaceName, String spaceProfileImg) {
        return Space.builder()
                .spaceName(spaceName)
                .spaceProfileImg(spaceProfileImg)
                .build();
    }

}
