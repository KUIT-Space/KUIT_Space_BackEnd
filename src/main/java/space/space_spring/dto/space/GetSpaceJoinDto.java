package space.space_spring.dto.space;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetSpaceJoinDto {

    private String spaceProfileImg;

    private String spaceName;

    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String spaceProfileImg;

        private String spaceName;

        private String createdAt;           // 스페이스 개설일의 정보를 yyyy년 mm월 dd일 형식으로 변환한 문자열

        private int memberNum;
    }
}
