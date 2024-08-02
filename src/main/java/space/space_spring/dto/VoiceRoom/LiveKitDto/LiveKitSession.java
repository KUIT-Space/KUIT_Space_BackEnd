package space.space_spring.dto.VoiceRoom.LiveKitDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class LiveKitSession {
    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("roomName")
    private String roomName;

    @JsonProperty("createdAt")
    private Instant createdAt;

    @JsonProperty("lastActive")
    private Instant lastActive;

    @JsonProperty("bandwidthIn")
    private long bandwidthIn;

    @JsonProperty("bandwidthOut")
    private long bandwidthOut;

    @JsonProperty("egress")
    private int egress;

    @JsonProperty("numParticipants")
    private int numParticipants;

    @JsonProperty("numActiveParticipants")
    private int numActiveParticipants;

    // Getters and setters for all fields

    @Override
    public String toString() {
        return "LiveKitSession{" +
                "sessionId='" + sessionId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", createdAt=" + createdAt +
                ", lastActive=" + lastActive +
                ", bandwidthIn=" + bandwidthIn +
                ", bandwidthOut=" + bandwidthOut +
                ", egress=" + egress +
                ", numParticipants=" + numParticipants +
                ", numActiveParticipants=" + numActiveParticipants +
                '}';
    }
}
