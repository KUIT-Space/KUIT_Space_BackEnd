package space.space_spring.dto.VoiceRoom;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParticipantListDto {


    final private List<ParticipantDto> participantDtoList;

    private ParticipantListDto(List<ParticipantDto> participantDtoList){
        this.participantDtoList=participantDtoList;
    }

    public static ParticipantListDto from(List<ParticipantDto> participantDtoList ){
        return new ParticipantListDto(participantDtoList);
    }
    public static  ParticipantListDto empty(){
        return new ParticipantListDto(Collections.emptyList());
    }

    public static ParticipantListDto nullList(){
        return new ParticipantListDto(null);
    }

    public void setProfileImage(Function<Long, String> profileFinder){
        this.participantDtoList.forEach(participantDto -> {
            String profileImage = profileFinder.apply(participantDto.getUserSpaceId());
            participantDto.setProfileImage(profileImage);
        });
    }



    //Todo 생성/변환 책임분리 필요
    public List<GetParticipantList.ParticipantInfo> convertParticipantDtoList(){
        if(this.participantDtoList==null){System.out.print("\n[DEBUG] participant List is NULL\n"); return null;}
        if(this.participantDtoList.isEmpty()){System.out.print("\n[DEBUG] participant List is Empty\n"); return Collections.emptyList();}
        return this.participantDtoList.stream()
                .map(GetParticipantList.ParticipantInfo::convertParticipantDto)
                .collect(Collectors.toList());
    }

    public List<ParticipantDto> getParticipantDtoList() {
        return participantDtoList;
    }
}
