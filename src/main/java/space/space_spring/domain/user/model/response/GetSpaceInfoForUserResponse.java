package space.space_spring.domain.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.domain.user.model.dto.SpaceChoiceInfo;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetSpaceInfoForUserResponse {

    private String userName;

    private Long lastUserSpaceId;

    private List<SpaceChoiceInfo> spaceInfoList;

}