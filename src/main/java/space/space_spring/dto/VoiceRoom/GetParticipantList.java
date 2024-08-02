package space.space_spring.dto.VoiceRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class GetParticipantList {
    @Getter
    public static class Request{
        private long roomId;
    }
    @AllArgsConstructor
    @Getter
    public static class Response{
        private List<ParticipantInfo> participantInfoList;

    }
    @Builder
    @Getter
    @Setter
    public static class ParticipantInfo{
        private String name;
        private boolean isMute;
        private String profileImage;

        public static ParticipantInfo convertParticipantDto(ParticipantDto participantDto){
            if(participantDto==null){return null;}
            return ParticipantInfo.builder()
                    .name(participantDto.getName())
                    .isMute(participantDto.isMicMute())
                    .build();
        }
        public static List<ParticipantInfo> convertParticipantDtoList(List<ParticipantDto> participantDtoList){
            if(participantDtoList==null||participantDtoList.isEmpty()){return null;}
            return participantDtoList.stream()
                    .map(ParticipantInfo::convertParticipantDto)
                    .collect(Collectors.toList());
        }
    }
}
