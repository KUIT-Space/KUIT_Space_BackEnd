package space.space_spring.dto.space;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.dto.userSpace.UserProfileImgAndNameDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetUserInfoBySpaceResponse {

    private List<UserProfileImgAndNameDto> userProfileImgAndNameDtoList = new ArrayList<>();
}
