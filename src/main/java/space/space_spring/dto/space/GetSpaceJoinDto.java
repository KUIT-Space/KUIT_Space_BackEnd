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

        private LocalDateTime createdAt;

        private int memberNum;
    }
}
