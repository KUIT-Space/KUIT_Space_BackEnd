package space.space_spring.domain.space.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.domain.userSpace.model.UserInfoInSpace;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetUserInfoBySpaceResponse {

    private List<UserInfoInSpace> userInfoInSpaceList = new ArrayList<>();
}
