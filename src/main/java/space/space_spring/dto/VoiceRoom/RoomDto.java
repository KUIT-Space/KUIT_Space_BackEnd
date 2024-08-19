package space.space_spring.dto.VoiceRoom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import livekit.LivekitModels;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import space.space_spring.entity.VoiceRoom;

import java.util.List;
import java.util.stream.Collectors;
@Builder
@Getter

public class RoomDto {

    private String name;
    private long id;
    private int numParticipants;
    private long startTime; //time Stamp
    private long createdAt;
    private List<ParticipantDto> participantDTOList;
    private int order;
    private String sid;
    private String metadata;


    public void setParticipantDTOListByInfo(List<LivekitModels.ParticipantInfo> participantInfoList){
        if(participantInfoList==null||participantInfoList.isEmpty()){return;}
        this.participantDTOList =  participantInfoList.stream()
                .map(ParticipantDto::convertParticipant)
                .collect(Collectors.toList());
    }
    public RoomDto setParticipantDTOList(List<ParticipantDto> participantDtoList){
        this.participantDTOList =  participantDtoList;
        return this;
    }

    public static RoomDto convertRoom(LivekitModels.Room room){
        if(room==null){return null;}
        return RoomDto.builder()
                .name(room.getName())
                .numParticipants(room.getNumParticipants())
                .startTime(room.getCreationTime())
                .sid(room.getSid())
                .metadata(room.getMetadata())
                .participantDTOList(null)
                .build();
    }

    public static List<RoomDto> convertRoomDtoListByVoiceRoom(List<VoiceRoom> voiceRoomList){
        if(voiceRoomList==null||voiceRoomList.isEmpty()){return null;}
        return voiceRoomList.stream()
                .map(RoomDto::convertRoom)
                .collect(Collectors.toList());
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
    public static RoomDto convertRoom(VoiceRoom voiceRoom){
        if(voiceRoom==null){return null;}
        return RoomDto.builder()
                .name(voiceRoom.getName())
                //.createdAt(voiceRoom.)
                .id(voiceRoom.getVoiceRoomId())
                .order(voiceRoom.getOrder())
                .sid(null)
                .metadata(null)
                //.startTime()
                .participantDTOList(null)
                .build();
    }

    public static List<RoomDto> convertRoomDtoListByRoom(List<LivekitModels.Room> liveKitRoomList){
//        if(liveKitRoomList==null||liveKitRoomList.isEmpty()){
//
//        }else if(!clazz.isInstance(liveKitRoomList.get(0))){
//            throw new IllegalArgumentException(
//                    "List contains an element of type " + liveKitRoomList.get(0).getClass().getSimpleName() +
//                            ", which is not an instance of " + clazz.getSimpleName()
//            );
//        }
        if(liveKitRoomList==null||liveKitRoomList.isEmpty()){return null;}
        return liveKitRoomList.stream()
                .map(RoomDto::convertRoom)
                .collect(Collectors.toList());

    }
    public void setActiveRoom(List<LivekitModels.Room> liveKitRoomList){
        if(liveKitRoomList==null||liveKitRoomList.isEmpty()){return;}
        boolean find = false;
        for(LivekitModels.Room resRoom : liveKitRoomList){
            if(String.valueOf(this.id).equals( resRoom.getName() )){
                this.numParticipants = resRoom.getNumParticipants();
                this.sid = resRoom.getSid();
                this.metadata = resRoom.getMetadata();
                this.startTime= resRoom.getCreationTime();
                find = true;
                break;
            }

        }
        if(!find){this.numParticipants=0;}
        //return this;
    }
    private static boolean EqualRoomIdByNameTag(String roomName,long Id){
        return roomName.endsWith("#"+String.valueOf(Id));
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
