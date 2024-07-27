package space.space_spring.dto.space;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSpaceCreateResponse {

    private Long spaceId;
    private String spaceImgUrl;         // 사진 url이 잘 생성됐는지 확인하는 용도
}
