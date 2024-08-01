package space.space_spring.dto.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SpaceChoiceViewDto {

    private List<SpaceChoiceInfo> spaceChoiceInfoList;

    private Long lastUserSpaceId;

}
