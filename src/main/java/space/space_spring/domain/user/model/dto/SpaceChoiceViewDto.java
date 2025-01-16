package space.space_spring.domain.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SpaceChoiceViewDto {

    private List<SpaceChoiceInfo> spaceChoiceInfoList;

    private Long lastUserSpaceId;

}
