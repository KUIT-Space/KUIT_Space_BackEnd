package space.space_spring.dto.space;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class PostSpaceCreateRequest {

    @Length(min = 1, max = 10, message = "이름은 10자이내의 문자열이어야 합니다.")
    private String spaceName;

    private String spaceProfileImg;         // 스페이스 프로필 이미지 (썸네일)
}
