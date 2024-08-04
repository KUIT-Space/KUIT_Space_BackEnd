package space.space_spring.dto.userSpace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoInSpace {

    private Long userId;

    private String userName;

    private String profileImgUrl;

    private String userAuth;                // 해당 스페이스에서의 유저 권한 (manager vs normal)
}
