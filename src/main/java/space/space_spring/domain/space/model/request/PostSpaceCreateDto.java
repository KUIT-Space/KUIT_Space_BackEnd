package space.space_spring.domain.space.model.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public class PostSpaceCreateDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        @Length(min = 1, max = 10, message = "이름은 10자이내의 문자열이어야 합니다.")
        @NotBlank(message = "스페이스 이름은 공백일 수 없습니다.")
        private String spaceName;

        @Nullable
        private MultipartFile spaceProfileImg;         // 스페이스 프로필 이미지 (썸네일)
    }

    @Getter
    @AllArgsConstructor
    public static class Response {

        private Long spaceId;
    }
}
