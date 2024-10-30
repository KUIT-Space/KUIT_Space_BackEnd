package space.space_spring.dto.VoiceRoom;

import livekit.LivekitModels;
import space.space_spring.entity.VoiceRoom;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VoiceRoomListDto {
    private List<RoomDto> roomDtoList;

    public VoiceRoomListDto(List<RoomDto> roomDtos){
        this.roomDtoList=roomDtos;
    }

    public static VoiceRoomListDto from(List<VoiceRoom> voiceRoomEntityList){
        List<RoomDto> roomDtos = convertRoomDtoListByVoiceRoom(voiceRoomEntityList);
        return new VoiceRoomListDto(roomDtos);
    }

    public static List<RoomDto> convertRoomDtoListByVoiceRoom(List<VoiceRoom> voiceRoomList){
        if(voiceRoomList==null||voiceRoomList.isEmpty()){return null;}
        return voiceRoomList.stream()
                .map(RoomDto::convertRoom)
                .collect(Collectors.toList());
    }

    private void setActiveRoom(Map<String,LivekitModels.Room> roomResponse){
        for(RoomDto room : this.roomDtoList){
            LivekitModels.Room resRoom= roomResponse.get(String.valueOf(room.getId()));
            if(resRoom==null){continue;}
            room.setActiveRoom(resRoom);
        }

    }
    public void setActiveRoom(List<LivekitModels.Room> roomResponse){
        Map<String,LivekitModels.Room> roomResMap=roomResponse.stream()
                .collect(Collectors.toMap(
                        res->res.getName(),
                        res->res,
                        (oldVal,newVal)->newVal
                ));
        roomResMap = Collections.unmodifiableMap(roomResMap);
        setActiveRoom(roomResMap);
    }

    public List<GetVoiceRoomList.VoiceRoomInfo> convertVoicRoomInfoList(Integer limit){
        if(this.roomDtoList==null||this.roomDtoList.isEmpty()){return null;}
        Stream<RoomDto> sortedStream = this.roomDtoList.stream()
                .sorted(Comparator
                        .comparing((RoomDto r) -> r.getNumParticipants() == 0) // Active rooms first
                        .thenComparing(RoomDto::getOrder)) ;// Then by order
        System.out.print("limit input:"+limit);
        Stream<RoomDto> processedStream = (limit != null) ? sortedStream.limit(limit) : sortedStream;

        return processedStream.map(GetVoiceRoomList.VoiceRoomInfo::convertRoomDto)
                .collect(Collectors.toList());
    }
    public List<GetVoiceRoomList.VoiceRoomInfo> convertVoicRoomInfoList() {
        return convertVoicRoomInfoList( null);
    }


}
