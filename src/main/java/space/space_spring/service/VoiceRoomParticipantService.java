package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dao.VoiceRoomRepository;
import space.space_spring.domain.user.repository.UserDao;
import space.space_spring.dto.VoiceRoom.GetParticipantList;
import space.space_spring.dto.VoiceRoom.ParticipantDto;
import space.space_spring.dto.VoiceRoom.ParticipantListDto;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.util.LiveKitUtils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoiceRoomParticipantService {
    final private UserSpaceDao userSpaceDao;
    final private UserDao userDao;
    final private VoiceRoomRepository voiceRoomRepository;
    final private LiveKitUtils liveKitUtils;
    public List<GetParticipantList.ParticipantInfo> getParticipantInfoListById(long voiceRoomId){
        return getParticipantDtoListById(voiceRoomId).convertParticipantDtoList();
    }
    private ParticipantListDto getParticipantDtoListById(long voiceRoomId){
        Space space = voiceRoomRepository.findById(voiceRoomId).getSpace();
        //Todo 다른 네이밍 고려
        List<ParticipantDto> participantDtos =  liveKitUtils.getParticipantInfo(String.valueOf(voiceRoomId));
        if(participantDtos==null || participantDtos.isEmpty()){
            participantDtos =  Collections.emptyList();
        }

        ParticipantListDto participantListDto = ParticipantListDto.from(participantDtos);


        participantListDto.setProfileImage(this::findProfileImageByUserId);
        return participantListDto;
    }
    private String findProfileImageByUserId(Long userSpaceId){
        return userSpaceDao.findProfileImageById(userSpaceId).orElse("");
    }
}
