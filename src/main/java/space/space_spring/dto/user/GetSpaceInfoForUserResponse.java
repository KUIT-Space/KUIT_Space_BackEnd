package space.space_spring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class GetSpaceInfoForUserResponse {

    private String userName;

    private Long lastUserSpaceId;

    private List<Map<String, String>> spaceInfoList;

}