package space.space_spring.dto.VoiceRoom;

import space.space_spring.entity.VoiceRoom;

import java.util.List;
import java.util.stream.Collectors;

public class VoiceRoomEntityList {

    final private List<VoiceRoom> voiceRoomEntityList;

    private VoiceRoomEntityList(List<VoiceRoom> voiceRoomEntityList) {
        this.voiceRoomEntityList = voiceRoomEntityList;
    }
    public VoiceRoomEntityList from(List<VoiceRoom> voiceRoomEntityList){
        return new VoiceRoomEntityList(voiceRoomEntityList);
    }
    public List<RoomDto> convertRoomDto(){
        if(this.voiceRoomEntityList==null||this.voiceRoomEntityList.isEmpty()){return null;}
        return this.voiceRoomEntityList.stream()
                .map(RoomDto::convertRoom)
                .collect(Collectors.toList());
    }
}