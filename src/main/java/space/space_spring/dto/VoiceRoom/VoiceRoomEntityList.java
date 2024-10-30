package space.space_spring.dto.VoiceRoom;

import space.space_spring.entity.VoiceRoom;

import java.util.List;
import java.util.stream.Collectors;

public class VoiceRoomEntityList {

    private List<VoiceRoom> voiceRoomEntityList;

    public void VoiceRoomList(List<VoiceRoom> voiceRoomEntityList) {
        this.voiceRoomEntityList = voiceRoomEntityList;
    }

    public List<RoomDto> convertRoomDto(){
        if(this.voiceRoomEntityList==null||this.voiceRoomEntityList.isEmpty()){return null;}
        return this.voiceRoomEntityList.stream()
                .map(RoomDto::convertRoom)
                .collect(Collectors.toList());
    }
}