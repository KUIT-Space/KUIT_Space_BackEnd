package space.space_spring.dto.userSpace;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class PutUserProfileInSpaceDto {

    private String userName;

    private String userProfileImg;

    private String userProfileMsg;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @Nullable
        private MultipartFile userProfileImg;

        @NotBlank(message = "유저 이름은 공백일 수 없습니다.")
        @Length(min = 1, max = 10, message = "이름은 10자이내의 문자열이어야 합니다.")
        private String userName;

        @Nullable
        @Length(max = 50, message = "프로필 상태메시지는 50자이내의 문자열이어야 합니다.")
        private String userProfileMsg;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {

        private String userProfileImg;

        private String userAuth;

        private String userName;

        private String userProfileMsg;
    }
}
