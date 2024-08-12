package space.space_spring.dto.VoiceRoom;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PostVoiceRoomDto {
    @Getter
    public static class Request{
        @NotNull(message = "name is  mandatory")
        private String name;
    }
    @Getter
    @AllArgsConstructor
    public static class Response{
        private Long voiceRoomId;
    }
}
