package space.space_spring.service;

import livekit.LivekitModels;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.VoiceRoomDao;
import space.space_spring.dao.VoiceRoomRepository;
import space.space_spring.dto.VoiceRoom.*;
import space.space_spring.entity.Space;
import space.space_spring.entity.VoiceRoom;
import space.space_spring.util.LiveKitUtils;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoiceRoomService {
    @Autowired
    private final VoiceRoomRepository voiceRoomRepository;
    private final VoiceRoomDao voiceRoomDao;
    private final SpaceUtils spaceUtils;
    private final LiveKitService liveKitService;
    private final LiveKitUtils liveKitUtils;
    private final UserDao userDao;

    public boolean createVoiceRoom(long spaceId,PostVoiceRoomDto.Request req){
        Space targetSpace = spaceUtils.findSpaceBySpaceId(spaceId);
        Integer orderInt = voiceRoomRepository.findMaxOrderBySpace(targetSpace);
        int order;
        if(orderInt==null||orderInt==0){
            order=1;
        }else{
            order=orderInt.intValue()+1;
        }
        String name = req.getName();

        return voiceRoomDao.createVoiceRoom(name, order, targetSpace);
    }

    public List<GetVoiceRoomList.VoiceRoomInfo> getVoiceRoomInfoList(long spaceId,GetVoiceRoomList.Request req){
        int limit = req.getLimit();
        boolean showParticipant =req.isShowParticipant();


        //ToDo 해당 space VoiceRoom 가져오기 (VoiceRoom List)
            //Todo 가져오기에 limit 적용
        List<VoiceRoom> voiceRoomDataList = findBySpaceId(spaceId);
        List<RoomDto> roomDtoList = RoomDto.convertRoomDtoListByVoiceRoom(voiceRoomDataList);
        //ToDo VoiceRoom과 Room mapping
            //#1 Response 받아오기
            List<LivekitModels.Room> roomResponses = liveKitUtils.getRoomList();
            //#2 Room과 mapping 시키기
            for(RoomDto roomDto : roomDtoList){
                roomDto.setActiveRoom(roomResponses);
            }
        //ToDo participant mapping
        if (showParticipant) {
            for(RoomDto roomDto : roomDtoList) {
                if(roomDto.getNumParticipants()==0){continue;}
                //participantDto List 가져오기
                List<ParticipantDto> participantDtoList = getParticipantDtoListById(roomDto.getId());
                for(ParticipantDto participantDto: participantDtoList){
                    //Todo profileIamge 집어넣기
                }
                //RoomDto에 값 집어넣기
                roomDto.setParticipantDTOList(participantDtoList);
            }
        }
        //ToDo Response로 convert
            //#1 Active/inActive 분리

            //#2 convert
            return GetVoiceRoomList.VoiceRoomInfo.convertRoomDtoList(roomDtoList);

        //return null;
    }

    public List<VoiceRoom> findBySpaceId(long spaceId){
        return findBySpace(spaceUtils.findSpaceBySpaceId(spaceId));
    }
    private List<VoiceRoom> findBySpace(Space space){
        return voiceRoomRepository.findBySpace(space);
    }
    private List<ParticipantDto> getParticipantDtoListById(long voiceRoomId){
        return liveKitUtils.getParticipantInfo(findNameTagById(voiceRoomId));
    }
    public List<GetParticipantList.ParticipantInfo> getParticipantInfoListById(long voiceRoomId){
        return GetParticipantList.ParticipantInfo.convertParticipantDtoList(
                liveKitUtils.getParticipantInfo(findNameTagById(voiceRoomId))
        );
    }
    private String findNameTagById(long id){
        VoiceRoom voiceRoom =voiceRoomRepository.findById(id);
        //null pointer error 처리
        String name = voiceRoom.getName();
        return name+" #"+String.valueOf(id);
    }

    public String getToken(long spaceId,long userId,long voiceRoomId){
        String userName=userDao.findUserByUserId(userId).getUserName();
        String userIdentity=String.valueOf(userId);
        String metadata="";
        String roomName=findNameTagById(voiceRoomId);
        return liveKitUtils.getRoomToken(userName,userIdentity,roomName,metadata).toJwt();
    }
}
