package space.space_spring.dto.VoiceRoom.LiveKitDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import space.space_spring.dto.VoiceRoom.LiveKitDto.LiveKitSession;

import java.util.List;

public class LiveKitSessionResponse {
    @JsonProperty("sessions")
    private List<LiveKitSession> sessions;
    public List<LiveKitSession> getSessions(){return sessions;}
}
