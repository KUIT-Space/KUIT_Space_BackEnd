package space.space_spring.domain.voiceRoom.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GetParticipantList {
    @Getter
    public static class Request{
        @NotNull(message = "roomId is  mandatory")
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
        private Long id;
        private Long userSpaceId;
        private boolean isMute;
        private String profileImage;

        public static ParticipantInfo convertParticipantDto(ParticipantDto participantDto){
            if(participantDto==null){return null;}
            return ParticipantInfo.builder()
                    .name(participantDto.getName())
                    .isMute(participantDto.isMicMute())
                    .profileImage(participantDto.getProfileImage())
                    .id(participantDto.getId())
                    .userSpaceId(participantDto.getUserSpaceId())
                    .build();
        }
        public static List<ParticipantInfo> convertParticipantDtoList(List<ParticipantDto> participantDtoList){
            if(participantDtoList==null){System.out.print("\n[DEBUG] participant List is NULL\n"); return null;}
            if(participantDtoList.isEmpty()){System.out.print("\n[DEBUG] participant List is Empty\n"); return Collections.emptyList();}
            return participantDtoList.stream()
                    .map(ParticipantInfo::convertParticipantDto)
                    .collect(Collectors.toList());
        }
    }
}
