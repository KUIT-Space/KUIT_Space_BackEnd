package space.space_spring.dto.space;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.validator.ValidImageFile;

@Getter
@Setter
@NoArgsConstructor
public class PostSpaceCreateRequest {

    @Length(min = 1, max = 10, message = "이름은 10자이내의 문자열이어야 합니다.")
    @NotBlank(message = "스페이스 이름은 공백일 수 없습니다.")
    private String spaceName;

    @ValidImageFile(message = "스페이스 프로필 이미지는 공백일 수 없습니다.")
    private MultipartFile spaceProfileImg;         // 스페이스 프로필 이미지 (썸네일)
}
