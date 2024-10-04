package space.space_spring.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetUserProfileListDto {

    @Getter
    @AllArgsConstructor
    public static class Response {

        private List<UserProfile> userProfileList;
    }

    @Getter
    @AllArgsConstructor
    public static class UserProfile {

        private Long spaceId;

        private String spaceName;

        private String userName;

        private String userProfileImg;

        private String userAuth;
    }
}
