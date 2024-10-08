package space.space_spring.dto.VoiceRoom;

import java.util.List;
import java.util.function.Function;

public class ParticipantListDto {


    private List<ParticipantDto> participantDtoList;

    private ParticipantListDto(List<ParticipantDto> participantDtoList){
        this.participantDtoList=participantDtoList;
    }
    public ParticipantListDto from(List<ParticipantDto> participantDtoList ){

        return new ParticipantListDto(participantDtoList);
    }

    public void setProfileImage(Function<Long, String> profileFinder){
        this.participantDtoList.forEach(participantDto -> {
            String profileImage = profileFinder.apply(participantDto.getUserSpaceId());
            participantDto.setProfileImage(profileImage);
        });
    }
}
