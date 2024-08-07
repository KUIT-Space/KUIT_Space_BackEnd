package space.space_spring.dto.VoiceRoom;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class PostVoiceRoomDto {
    @Getter
    public static class Request{
        @NotNull(message = "name is  mandatory")
        private String name;
    }
    @Getter
    public static class Response{

    }
}
