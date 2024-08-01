package space.space_spring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpaceChoiceInfo {

    private Long spaceId;

    private String spaceName;

    private String profileImgUrl;
}