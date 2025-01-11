package space.space_spring.domain.voiceRoom.model.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class GetVoiceRoomList {
    @Getter
    @AllArgsConstructor
    public static class Request{
        @Nullable
        private int limit;
        @Nullable
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
        private String metadate;

        public static VoiceRoomInfo convertRoomDto(RoomDto roomDto){
            if(roomDto==null){return null;}
            return VoiceRoomInfo.builder()
                    .name(roomDto.getName())
                    .id(roomDto.getId())
                    .Active(roomDto.getNumParticipants()!=0)
                    .numParticipant(roomDto.getNumParticipants())
                    .order(roomDto.getOrder())
                    .metadate(roomDto.getMetadata())
                    .participantInfoList(
                            GetParticipantList.ParticipantInfo.convertParticipantDtoList(roomDto.getParticipantListDto().getParticipantDtoList())
                    )
                    .build();
        }
    }
}
