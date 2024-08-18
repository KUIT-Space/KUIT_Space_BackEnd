package space.space_spring.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostLoginDto {

    private String jwt;

    private Long userId;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        // '@', '.' 이 있어야 함
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        @NotBlank
        private String email;

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
                message = "8~16글자의 영문 대/소문자, 숫자, 특수문자가 포함되어야 합니다."
        )
        @NotBlank
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {

        private Long userId;
    }
}
