package space.space_spring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class SpaceChoiceViewDto {

    private List<SpaceChoiceInfo> spaceChoiceInfoList;

    private Long lastUserSpaceId;

}
