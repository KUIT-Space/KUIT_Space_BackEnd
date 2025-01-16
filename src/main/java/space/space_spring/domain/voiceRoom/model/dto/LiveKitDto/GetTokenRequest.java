package space.space_spring.domain.voiceRoom.model.dto.LiveKitDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTokenRequest {
    private String name;
    private String roomName;
    private String identity;
    private String metadata;
}
