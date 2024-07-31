package space.space_spring.dto.VoiceRoom;

import lombok.Getter;

public class PostVoiceRoomDto {
    @Getter
    public static class Request{
        private long spaceId;
        private String name;
    }
    @Getter
    public static class Response{

    }
}
