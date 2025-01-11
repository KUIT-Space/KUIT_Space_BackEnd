package space.space_spring.domain.voiceRoom.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostVoiceRoomDto {
    @Getter
    //@Builder
    @AllArgsConstructor
    @NoArgsConstructor
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
