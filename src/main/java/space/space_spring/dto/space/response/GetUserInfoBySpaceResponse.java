package space.space_spring.dto.space.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.dto.userSpace.UserInfoInSpace;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetUserInfoBySpaceResponse {

    private List<UserInfoInSpace> userInfoInSpaceList = new ArrayList<>();
}
