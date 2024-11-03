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
    //private List<ParticipantDto> participantDTOList;
    private ParticipantListDto participantListDto;
    private int order;
    private String sid;
    private String metadata;


//    public void setParticipantDTOListByInfo(List<LivekitModels.ParticipantInfo> participantInfoList){
//        if(participantInfoList==null||participantInfoList.isEmpty()){return;}
//        this.participantDTOList =  participantInfoList.stream()
//                .map(ParticipantDto::convertParticipant)
//                .collect(Collectors.toList());
//    }
    public RoomDto setParticipantDTOList(List<ParticipantDto> participantDtoList){
        this.participantListDto=  ParticipantListDto.from(participantDtoList);
        return this;
    }

    public RoomDto setParticipantDTOList(ParticipantListDto participants){
        this.participantListDto =  participants;
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
                .participantListDto(null)
                .build();
    }

    //Todo VoiceRoomDtoList 로 이전 예정
    public static List<RoomDto> convertRoomDtoListByVoiceRoom(List<VoiceRoom> voiceRoomList){
        if(voiceRoomList==null||voiceRoomList.isEmpty()){return null;}
        return voiceRoomList.stream()
                .map(RoomDto::convertRoom)
                .collect(Collectors.toList());
    }

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
                .participantListDto(null)
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
                setActiveRoom(resRoom,true);
                find = true;
                break;
            }

        }
        if(!find){this.numParticipants=0;}
        //return this;
    }

    public void setActiveRoom(LivekitModels.Room resRoom,boolean checkId){
        //null에 대한 예외 처리
        if(checkId || String.valueOf(this.id).equals( resRoom.getName() )){
            this.numParticipants = resRoom.getNumParticipants();
            this.sid = resRoom.getSid();
            this.metadata = resRoom.getMetadata();
            this.startTime= resRoom.getCreationTime();

        }
    }
    public void setActiveRoom(LivekitModels.Room resRoom){
        setActiveRoom(resRoom,false);
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

    public ParticipantListDto getParticipantListDto(){return participantListDto;}
    public List<ParticipantDto> getParticipantDtoList(){return participantListDto.getParticipantDtoList();}
}
