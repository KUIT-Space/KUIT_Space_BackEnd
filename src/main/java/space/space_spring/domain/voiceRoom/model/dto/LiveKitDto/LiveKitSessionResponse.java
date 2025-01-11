package space.space_spring.domain.voiceRoom.model.dto.LiveKitDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LiveKitSessionResponse {
    @JsonProperty("sessions")
    private List<LiveKitSession> sessions;
    public List<LiveKitSession> getSessions(){return sessions;}
}
