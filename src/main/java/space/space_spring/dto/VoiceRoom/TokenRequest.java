package space.space_spring.dto.VoiceRoom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {
    private String name;
    private String roomName;
    private String identity;
    private String metadata;
}
