package space.space_spring.dto.VoiceRoom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import livekit.LivekitModels;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ParticipantDto {
    private String id;
    private String name;
    private boolean isMicMute;


    private LivekitModels.ParticipantInfo.State state; // int
    private String metadata;
    private long joinAt; //time stamp


    public static ParticipantDto convertParticipant(LivekitModels.ParticipantInfo participantInfo){
        return ParticipantDto.builder()
                .id(participantInfo.getIdentity())
                .name(participantInfo.getName())
                .isMicMute(checkMicMute(participantInfo.getTracksList()))

                .state(participantInfo.getState())
                .metadata(participantInfo.getMetadata())
                .joinAt(participantInfo.getJoinedAt())

                .build();
    }
    public static List<ParticipantDto> convertParticipantList(List<LivekitModels.ParticipantInfo> participantInfoList){
        return participantInfoList.stream()
                .map(ParticipantDto::convertParticipant)
                .collect(Collectors.toList());
    }

    private static boolean checkMicMute(List<LivekitModels.TrackInfo> trackList){
        return trackList.stream()
                .filter(ParticipantDto::isAudio)
                .anyMatch(LivekitModels.TrackInfo::getMuted);
    }
    private static boolean isAudio(LivekitModels.TrackInfo track){
        return isAudioString(track.getName()) ||
                isAudioString(track.getType().toString()) ||
                isAudioString(track.getSource().toString());
    }
    private static boolean isAudioString(String typeName){
        if(typeName==null){
            return false;
        }
        String lowercase = typeName.toLowerCase();
        return lowercase.contains("mic") || lowercase.contains("audio");
    }

    private static final ObjectMapper objectMapper=new ObjectMapper();
    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return String.format("{\"error\": \"Failed to convert object to JSON: %s\"}", e.getMessage());
        }
    }
}

