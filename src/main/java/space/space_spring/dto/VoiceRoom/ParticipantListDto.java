package space.space_spring.dto.VoiceRoom;

import java.util.List;

public class ParticipantListDto {


    private List<ParticipantDto> participantDtoList;

    private ParticipantListDto(List<ParticipantDto> participantDtoList){
        this.participantDtoList=participantDtoList;
    }
    public ParticipantListDto from(List<ParticipantDto> participantDtoList ){

        return new ParticipantListDto(participantDtoList);
    }
}
