package space.space_spring.dto.VoiceRoom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import livekit.LivekitModels;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
@Builder
@Getter
public class RoomDto {

    private String name;
    private int numParticipants;
    private long creationTime; //time Stamp
    private List<ParticipantDto> participantDTOList;
    private String sid;
    private String metadata;

    public void setParticipantDTOList(List<LivekitModels.ParticipantInfo> participantInfoList){
        this.participantDTOList =  participantInfoList.stream()
                .map(ParticipantDto::convertParticipant)
                .collect(Collectors.toList());
    }

    public static RoomDto convertRoom(LivekitModels.Room room){
        return RoomDto.builder()
                .name(room.getName())
                .numParticipants(room.getNumParticipants())
                .creationTime(room.getCreationTime())
                .sid(room.getSid())
                .metadata(room.getMetadata())
                .participantDTOList(null)
                .build();
    }
//    public static Room convertRoom(LiveKitSession session){
//        return Room.builder()
//                .name(session.)
//                .numParticipants(room.getNumParticipants())
//                .creationTime(room.getCreationTime())
//                .sid(room.getSid())
//                .metadata(room.getMetadata())
//                .participantList(null)
//                .build();
//    }

    public static List<RoomDto> convertRoomList(List<LivekitModels.Room> liveKitRoomList){
        return liveKitRoomList.stream()
                .map(RoomDto::convertRoom)
                .collect(Collectors.toList());

    }

    private static final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return String.format("{\"error\": \"Failed to convert object to JSON: %s\"}", e.getMessage());
        }
    }

}
