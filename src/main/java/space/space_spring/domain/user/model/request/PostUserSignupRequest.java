package space.space_spring.domain.user.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserSignupRequest {

    // '@', '.' 이 있어야 함
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    @NotBlank
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "비밀번호에는 8~16글자의 영문 대/소문자, 숫자, 특수문자가 포함되어야 합니다."
    )
    @NotBlank
    private String password;

    @Length(min = 1, max = 10, message = "이름은 10자이내의 문자열이어야 합니다.")
    @NotBlank
    private String userName;
}
