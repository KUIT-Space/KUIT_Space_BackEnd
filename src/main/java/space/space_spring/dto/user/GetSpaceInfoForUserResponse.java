package space.space_spring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GetSpaceInfoForUserResponse {

    private String spaceName;

    private String spaceImgUrl;
}
