package space.space_spring.dto.userSpace;

import com.amazonaws.services.ec2.model.CpuOptionsRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserProfileInSpaceDto {

    @Getter
    @AllArgsConstructor
    public static class Response {

        private String userProfileImg;

        private String userName;

        private String userAuth;

        private String userProfileMsg;
    }
}
