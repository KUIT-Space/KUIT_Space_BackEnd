package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dao.VoiceRoomRepository;
import space.space_spring.domain.user.repository.UserDao;
import space.space_spring.dto.VoiceRoom.GetParticipantList;
import space.space_spring.dto.VoiceRoom.ParticipantDto;
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
        return GetParticipantList.ParticipantInfo.convertParticipantDtoList(
                getParticipantDtoListById(voiceRoomId)
                //liveKitUtils.getParticipantInfo(findNameTagById(voiceRoomId))
        );
    }
    private List<ParticipantDto> getParticipantDtoListById(long voiceRoomId){
        Space space = voiceRoomRepository.findById(voiceRoomId).getSpace();
        List<ParticipantDto> participantDtoList =  liveKitUtils.getParticipantInfo(String.valueOf(voiceRoomId));
        if(participantDtoList==null||participantDtoList.isEmpty()){
            return Collections.emptyList();
        }
        for(ParticipantDto participantDto: participantDtoList){
            //profileIamge 집어넣기
            participantDto.setProfileImage(findProfileImageByUserId(participantDto.getUserSpaceId()));
            //userSpaceId 집어 넣기
            User user = userDao.findUserByUserId(participantDto.getId());
            participantDto.setUserSpaceId(userSpaceDao.findUserSpaceByUserAndSpace(user,space).get().getUserSpaceId());
        }
        return participantDtoList;
    }
    private String findProfileImageByUserId(Long userSpaceId){
        return userSpaceDao.findProfileImageById(userSpaceId).orElse("");
    }
}
