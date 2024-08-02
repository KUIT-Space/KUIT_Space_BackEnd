package space.space_spring.dto.VoiceRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class GetVoiceRoomList {
    @Getter
    public static class Request{
        private int limit;
        private boolean showParticipant;
    }
    @AllArgsConstructor
    @Getter
    public static class Response{
        private List<VoiceRoomInfo> voiceRoomList;

    }
    @Builder
    @Getter
    public static class VoiceRoomInfo{
        private String name;
        private long id;
        private boolean Active;
        private int numParticipant;
        private int order;
        private List<GetParticipantList.ParticipantInfo> participantInfoList;

        public static VoiceRoomInfo convertRoomDto(RoomDto roomDto){
            if(roomDto==null){return null;}
            return VoiceRoomInfo.builder()
                    .name(roomDto.getName())
                    .id(roomDto.getId())
                    .Active(roomDto.getNumParticipants()!=0)
                    .numParticipant(roomDto.getNumParticipants())
                    .order(roomDto.getOrder())
                    .participantInfoList(
                            GetParticipantList.ParticipantInfo.convertParticipantDtoList(roomDto.getParticipantDTOList())
                    )
                    .build();
        }
        public static List<VoiceRoomInfo> convertRoomDtoList(List<RoomDto> roomDtoList){
            if(roomDtoList==null||roomDtoList.isEmpty()){return null;}
            return roomDtoList.stream()
                    .map(VoiceRoomInfo::convertRoomDto)
                    .collect(Collectors.toList());
        }
    }
}
