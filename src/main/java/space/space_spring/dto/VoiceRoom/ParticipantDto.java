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
    private Long id;
    private Long userSpaceId;
    private String name;
    private boolean isMicMute;


    private LivekitModels.ParticipantInfo.State state; // int
    private String metadata;
    private long joinAt; //time stamp
    private String profileImage;

    public void setProfileImage(String imageUrl){
        this.profileImage = imageUrl;
    }
    public void setUserSpaceId(long userSpaceId){this.userSpaceId=userSpaceId;}

    public static ParticipantDto convertParticipant(LivekitModels.ParticipantInfo participantInfo){
        if(participantInfo==null){return null;}
        Long id = Long.valueOf(participantInfo.getIdentity());
        Long userSpaceId = id;
        return ParticipantDto.builder()
                .id(id)
                .userSpaceId(userSpaceId)
                .name(participantInfo.getName())
                .isMicMute(checkMicMute(participantInfo.getTracksList()))

                .state(participantInfo.getState())
                .metadata(participantInfo.getMetadata())
                .joinAt(participantInfo.getJoinedAt())

                .build();
    }
    public static List<ParticipantDto> convertParticipantList(List<LivekitModels.ParticipantInfo> participantInfoList){
        if(participantInfoList==null||participantInfoList.isEmpty()){return null;}
        return participantInfoList.stream()
                .map(ParticipantDto::convertParticipant)
                .collect(Collectors.toList());
    }

    private static boolean checkMicMute(List<LivekitModels.TrackInfo> trackList){
        if(trackList==null||trackList.isEmpty()){return true;}
        return trackList.stream()
                .filter(ParticipantDto::isAudio)
                .anyMatch(LivekitModels.TrackInfo::getMuted);
    }
    private static boolean isAudio(LivekitModels.TrackInfo track){
        if(track==null){return false;}
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

