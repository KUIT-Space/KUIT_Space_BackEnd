package space.space_spring.dto.VoiceRoom;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LiveKitSessionResponse {
    @JsonProperty("sessions")
    private List<LiveKitSession> sessions;
    public List<LiveKitSession> getSessions(){return sessions;}
}
